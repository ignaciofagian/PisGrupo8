#import "AppDelegate.h"
#import "BLController.h"
#import "WSManager.h"
#import "MPSMainTabBarController.h"
#import "MPSQuestionsViewController.h"
#import "DataAnswer.h"
#import "WSResponse.h"
#import <Foundation/Foundation.h>
#import "DBManager.h"
#import "AFNetworkActivityIndicatorManager.h"

@interface AppDelegate ()

@property (strong, nonatomic) MPSMainTabBarController *tabBarController;

@end

@implementation AppDelegate




- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    BOOL okInit = YES;
    
    @try {
        self.restrictRotation = YES;

        // So AFNetworking automatically manages the activity indicator on status bar
        [[AFNetworkActivityIndicatorManager sharedManager] setEnabled:YES];
        
        //NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        //[defaults setBool:NO forKey:@"isOnTimeMachineMode"];
        
        [DBManager sharedInstance];
        BLController *blController = [BLController sharedInstance];
        WSManager *wsManager = [WSManager sharedInstance];
        
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
        self.tabBarController = [storyboard instantiateInitialViewController];
        MPSQuestionsViewController *questionsViewController = self.tabBarController.viewControllers[0];
        
        if (blController.userId == nil)
        {
            //User is not registered so we show Login view
            [self showLoginView];
        }
        else
        {
            //TODO: BORRAR
            //[blController setUserId:@"209E6E09-EF51-4DD7-90DC-73EAFDA4924A"];
            
            
            //User is registered so we start cashHistoryUpdater if not on time machine mode
            [blController startCashHistoryUpdaterIfNeeded];
            
            WSResponse *answeredQuestionsResponse = [[WSManager sharedInstance] getAnswersSynchronous];
            
            if (answeredQuestionsResponse.error != nil)
            {
                okInit = NO;
                NSLog(@"WS_Exception: %@", answeredQuestionsResponse.error);
            }
            else
            {
                NSArray *answeredQuestions = answeredQuestionsResponse.data;
                BOOL answeredGeneralQuestions = answeredQuestions.count > 0;
                questionsViewController.lastConfirmedAnswer = answeredQuestions.count;
                
                if (!answeredGeneralQuestions)
                {
                    //Show questions view
                    WSResponse *generalQuestionsResponse = [wsManager getGeneralQuestionsSynchronous];
                    
                    if (generalQuestionsResponse.error != nil)
                    {
                        okInit = NO;
                        NSLog(@"WS_Exception: %@", generalQuestionsResponse.error);
                    }
                    else
                    {
                        questionsViewController.questions = [generalQuestionsResponse.data mutableCopy];
                        [self.tabBarController setSelectedIndex:0];
                    }
                }
                else
                {
                    WSResponse *specificQuestionsResponse = [wsManager getSpecificQuestionsSynchronous];
                    if (specificQuestionsResponse.error != nil)
                    {
                        okInit = NO;
                        NSLog(@"WS_Exception: %@", specificQuestionsResponse.error);
                    }
                    else
                    {
                        NSArray *specificQuestions = specificQuestionsResponse.data;
                        BOOL answeredAllSpecificQuestions = specificQuestions.count == 0;
                        questionsViewController.questions = [[answeredQuestions arrayByAddingObjectsFromArray: specificQuestions] mutableCopy];
                        
                        //If all questions had been answered show chart, else show questions
                        answeredAllSpecificQuestions ? [self.tabBarController setSelectedIndex:1] : [self.tabBarController setSelectedIndex:0];
                    }
                }
            }
            
            if (okInit)
            {
                self.window.rootViewController = self.tabBarController;
            }
            else
            {
                [[BLController sharedInstance] showConnectionErrorAlert];
                [self showLoginView];
            }
        }
    }
    @catch (NSException *e) {
        NSLog(@"Error al iniciar la aplicacion: %@", e);
        [[BLController sharedInstance] showConnectionErrorAlert];
        [self showLoginView];
    }

    return YES;
}


- (void)showQuestionsViewController:(BOOL)isReset
{
    BOOL ok = YES;
    
    if (self.tabBarController == nil)
        self.tabBarController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateInitialViewController];
    
    MPSQuestionsViewController *questionsViewController = self.tabBarController.viewControllers[0];
    if (questionsViewController.questions == nil || questionsViewController.questions.count == 0 || isReset)
    {
        WSResponse *generalQuestionsResponse = [[WSManager sharedInstance] getGeneralQuestionsSynchronous];
        
        if (generalQuestionsResponse.error != nil)
        {
            ok = NO;
            [[BLController sharedInstance] showConnectionErrorAlert];
        }
        else
        {
            questionsViewController.questions = [generalQuestionsResponse.data mutableCopy];
            questionsViewController.lastConfirmedAnswer = 0;
        }
    }
    
    if (ok)
    {
        if (self.window.rootViewController != self.tabBarController)
            self.window.rootViewController = self.tabBarController;
        
        [self.tabBarController setSelectedIndex:0];
    }
}

- (void)showLoginView
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    UIViewController* rootController = [storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
    UINavigationController* navigation = [[UINavigationController alloc] initWithRootViewController:rootController];
    [self.window setRootViewController:navigation];
}

-(BOOL)eraseDataAndRestartCurrentGame:(BOOL)isTimeMachineMode
{
    BOOL success = YES;
    
    @try
    {
        //stop cash updater timer
        [[BLController sharedInstance] stopCashHistoryUpdater];
        
        success = [[DBManager sharedInstance] resetDB];
        if (success)
        {
            [[WSManager sharedInstance] resetQuestionsSynchronous];
            [BLController sharedInstance].userId = nil;
            [BLController sharedInstance].lastDatePersisted = 0;
            
            if (!isTimeMachineMode)
            {
                [BLController sharedInstance].isOnTimeMachineMode = NO;
                [BLController sharedInstance].timeMachineDate = [NSDate date];
            }
            
            //Reset the tab bar controller
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
            //first change window rootViewController to login then restart tabBarController
            [self showLoginView];
            self.tabBarController = [storyboard instantiateInitialViewController];
        }
        else
        {
            [[BLController sharedInstance] showDBErrorAlert];
        }
    }
    @catch (NSException *exception) {
        success = NO;
    }
    @finally {
        //do nothing
    }
    
    return success;
}



- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    NSLog(@"APPLICATION WILL RESIGN ACTIVE");
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    NSLog(@"APPLICATION DID ENTER BACKGROUND");
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    NSLog(@"APPLICATION WILL ENTER FOREGROUND");
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    NSLog(@"APPLICATION DID BECOME ACTIVE");
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

-(UIInterfaceOrientationMask)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window
{
    if(self.restrictRotation)
        return UIInterfaceOrientationMaskPortrait;
    else
        return UIInterfaceOrientationMaskAll;
}

@end
