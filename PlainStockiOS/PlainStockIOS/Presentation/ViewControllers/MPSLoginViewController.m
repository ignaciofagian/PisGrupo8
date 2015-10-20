//
//  ViewController.m
//  PlainStockiOS
//
//  Created by nacho on 9/6/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSLoginViewController.h"
#import "AppDelegate.h"
#import "BLController.h"
#import "DBManager.h"

@interface MPSLoginViewController ()

@end

@implementation MPSLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}




#pragma mark - Actions

- (IBAction)startButtonPressed:(id)sender
{
    //Get unique identifier as userID
    NSString *userID = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    [[DBManager getSharedInstance] createDB];
    [[DBManager getSharedInstance] saveDate:@"2015-10-05 07:06" Saldo:@"150.5"];
    [[DBManager getSharedInstance] saveDate:@"2015-10-05 07:08" Saldo:@"18.3"];
    [[DBManager getSharedInstance] saveDate:@"2015-10-05 07:01" Saldo:@"320.1"];
    [[DBManager getSharedInstance] saveDate:@"2015-10-05 18:54" Saldo:@"10.4"];
    WSResponse *registerResponse = [[BLController getInstance] registerUserWithID:userID];
    //NSMutableArray *resultados = [[DBManager getSharedInstance] readInformationFrom:@"2015-10-05 07:08" To:@"2015-10-05 21:08"];
    
    if (registerResponse.error == NULL)
    {
        //Register userID on NSUserDefaults
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:userID forKey:@"userId"];
        [defaults synchronize];
        [BLController getInstance].userId = userID;
        
        //Show the Questions view
        AppDelegate *appDelegateTemp = [[UIApplication sharedApplication]delegate];
        UITabBarController *tabBarController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateInitialViewController];
        [tabBarController setSelectedIndex:0];
        appDelegateTemp.window.rootViewController = tabBarController;
    }
    else
    {
        //TODO: control de errores
    }
}


@end
