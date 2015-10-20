//
//  AppDelegate.m
//  PlainStockiOS
//
//  Created by nacho on 9/6/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "AppDelegate.h"
#import "BLController.h"
#import "MPSMainTabBarController.h"
#import "DataAnswer.h"
#import "WSResponse.h"

@interface AppDelegate ()

@end

@implementation AppDelegate




- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    
    //TODO: CHEQUEOS DE ERROR WS, LLAMAR WS SALDOS
    
    self.restrictRotation = YES;
    
    BLController *mainController = [BLController getInstance];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    // Set language, english is default
    NSString *language = [defaults objectForKey:@"language"];
    
    if (!language)
    {
        language = @"ENG";
        [defaults setObject:language forKey:@"language"];
        [defaults synchronize];
    }
    else
        if ([language isEqualToString:@"SPA"])
            mainController.language = SPA; //ENG is the default language
    
    
    // See if user is registered and show view accordingly
    NSString *userId = [defaults objectForKey:@"userId"];
    BOOL isUserRegistered = userId != NULL;
    
    //TODO: BORRAR, DEBUG, LLAMA UN WS QUE BORRA AL USUARIO DEL SERVER
//    if (isUserRegistered)
//    {
//        [mainController deleteUserSynchronous:userId];
//        userId = NULL;
//        isUserRegistered = NO;
//    }
    //FIN BORRAR
    
    if (!isUserRegistered)
    {
        //Show Login view
        WSResponse *generalQuestionsResponse = [mainController getGeneralQuestionsSynchronous]; //TODO: chequeos de error
        mainController.questions = [generalQuestionsResponse.data mutableCopy];
        UIViewController* rootController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"LoginViewController"];
        UINavigationController* navigation = [[UINavigationController alloc] initWithRootViewController:rootController];
        
        [self.window setRootViewController:navigation];
    }
    else
    {
        mainController.userId = userId;
        
        MPSMainTabBarController *tabBarController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateInitialViewController];
        WSResponse *answeredQuestionsResponse = [mainController getAnswersSynchronous: mainController.userId]; //TODO: chequeos de error
        NSArray *answeredQuestions = answeredQuestionsResponse.data;
        BOOL answeredGeneralQuestions = answeredQuestions.count > 0;
        
        if (!answeredGeneralQuestions)
        {
            WSResponse *generalQuestionsResponse = [mainController getGeneralQuestionsSynchronous]; //TODO: chequeos de error
            mainController.questions = [generalQuestionsResponse.data mutableCopy];
            
            // Show Questions view, first questions
            [tabBarController setSelectedIndex:0];
            self.window.rootViewController = tabBarController;
        }
        else
        {
            WSResponse *specificQuestionsResponse = [mainController getSpecificQuestionsSynchronous: mainController.userId];
            NSArray *specificQuestions = specificQuestionsResponse.data;
            
            mainController.numberOfAnsweredQuestions = answeredQuestions.count;
            BOOL answeredAllSpecificQuestions = specificQuestions.count == 0;
            
            if (!answeredAllSpecificQuestions)
            {
                mainController.questions = [[answeredQuestions arrayByAddingObjectsFromArray: specificQuestions] mutableCopy];
                
                //Show Questions view, first non answered question
                [tabBarController setSelectedIndex:0];
                self.window.rootViewController = tabBarController;
            }
            else
            {
                mainController.questions = [answeredQuestions mutableCopy];
                //mainController.progressDelegate = [tabBarController.viewControllers objectAtIndex:1];
                // Show Progress view
                [tabBarController setSelectedIndex:1];
                self.window.rootViewController = tabBarController;
            }
        }
    }
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    NSLog(@"APPLICATION DID ENTER BACKGROUND");
    [[BLController getInstance] setIsAppInBackground:true];
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    NSLog(@"APPLICATION WILL ENTER FOREGROUND");
    [[BLController getInstance] setIsAppInBackground:false];
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
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
