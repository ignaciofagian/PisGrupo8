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
    //Register userID on NSUserDefaults
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userID = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    [defaults setObject:userID forKey:@"userId"];
    [defaults synchronize];
    
    //Call login service on server
    //TODO: primero se debe chequear conectividad, esto aplica a todos los ws no solo aca
    //      en caso de no conectividad se le muestra una UIAlertView, tmb hay que poner
    //      ActivityIndicator para todos los llamados a ws
    //TODO: Hay que arreglar la respuesta del login, tira error unprocessable response porque es texto plano en ves de json
    //      probablemente la solucion es agregar un descriptor
    [[BLController getInstance] registerUserWithID: userID];
    
    
    
    //Show the Questions view
    AppDelegate *appDelegateTemp = [[UIApplication sharedApplication]delegate];
    
    UITabBarController *tabBarController = [[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateInitialViewController];
    appDelegateTemp.window.rootViewController = tabBarController;

}


#pragma mark - BLControllerDelegate

- (void)userRegistrationFinished:(NSString *)msg
{
    NSLog(@"User registration finished with message: %@", msg);
}


@end
