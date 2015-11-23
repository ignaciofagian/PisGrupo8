#import "MPSLoginViewController.h"
#import "AppDelegate.h"
#import "BLController.h"

@interface MPSLoginViewController ()

@end

@implementation MPSLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated
{
    Language lang = [BLController sharedInstance].language;
    self.bodyLabel.text = (lang == ENG) ? @"Start living easily the world of the stock market" : @"Comienza a vivir de manera fácil el mundo del mercado de valores";
    NSString *startButtonText = (lang == ENG) ? @"Start" : @"Comenzar";
    [self.startButton setTitle:startButtonText forState:UIControlStateNormal];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark - Actions

- (IBAction)startButtonPressed:(id)sender
{
    //Not registered
    if ([BLController sharedInstance].userId == nil)
    {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *userID = [defaults objectForKey:@"userId"];
        
        //if it is not nil then user has been resetted and we keep the same ID
        BOOL reset = userID != nil;
        
        if (!reset)
            userID = [[NSUUID UUID] UUIDString];
        
        WSResponse *registerResponse = [[WSManager sharedInstance] registerUserWithID:userID];
    
        if (registerResponse.error == nil)
        {
            //Register userID on NSUserDefaults
            
            [defaults setObject:userID forKey:@"userId"];
            [defaults synchronize];
            [BLController sharedInstance].userId = userID;
            
            //Show the Questions view
            AppDelegate *appDelegateTemp = [[UIApplication sharedApplication]delegate];
            [appDelegateTemp showQuestionsViewController:reset];
        }
        else
        {
            [[BLController sharedInstance] showConnectionErrorAlert];
        }
    }
    else //User entered time machine or just reseted on real time or ws error occurred
    {
        //Show the Questions view
        AppDelegate *appDelegateTemp = [[UIApplication sharedApplication]delegate];
        [appDelegateTemp showQuestionsViewController:YES];
    }
    
    [[BLController sharedInstance] startCashHistoryUpdaterIfNeeded];
}


@end
