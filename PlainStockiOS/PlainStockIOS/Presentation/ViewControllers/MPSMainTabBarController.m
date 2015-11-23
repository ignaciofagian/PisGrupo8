//
//  MPSMainTabBarController.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/7/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSMainTabBarController.h"

@implementation MPSMainTabBarController 

- (void) viewDidLoad
{
    self.delegate = self;
    self.userAnsweredFirstQuestions = [[NSUserDefaults standardUserDefaults] objectForKey:@"answeredFirstQuestions"] != NULL;
}


- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    
    //TODO: descomentar una ves se guarde en NSUserDefaults o BD el userAnsweredFirstQuestions
    // Si no contesto las preguntas generales no se le permite cambiar de vista
    //if (self.userAnsweredFirstQuestions)
        return true;
    //else
        //return false;
}

@end
