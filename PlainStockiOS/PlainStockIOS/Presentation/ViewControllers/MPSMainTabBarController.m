//
//  MPSMainTabBarController.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/7/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSMainTabBarController.h"
#import "BLController.h"

@implementation MPSMainTabBarController 

- (void) viewDidLoad
{
    [super viewDidLoad];
    
    self.delegate = self;
    
    Language lang = [BLController getInstance].language;
    
    [[self.tabBar.items objectAtIndex:0] setTitle:(lang == ENG) ? @"Questions" : @"Preguntas"];
    [[self.tabBar.items objectAtIndex:1] setTitle:(lang == ENG) ? @"Evolution" : @"Evolución"];
    [[self.tabBar.items objectAtIndex:2] setTitle:(lang == ENG) ? @"Details" : @"Detalles"];
    [[self.tabBar.items objectAtIndex:3] setTitle:(lang == ENG) ? @"Settings" : @"Config"];
}


- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    return YES;

}

@end
