//
//  BLController.h
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/3/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol BLControllerDelegate <NSObject>

@optional
- (void)questionsResponseReceived:(NSArray *)questions;
- (void)userRegistrationFinished:(NSString *)msg;

@end



@interface BLController : NSObject

+ (BLController  *)getInstance;

@property (strong, nonatomic) NSString *userId;
@property (nonatomic) NSTimeInterval *systemDateOffset; //Seconds between system date and current date
@property (strong, nonatomic) NSArray *questions;
@property (strong, atomic) NSMutableArray *cashHistory;


- (void) registerUserWithID:(NSString *)userID;
- (NSArray *) getGeneralQuestions;
- (NSMutableArray *) getCashHistory;

@property (weak, nonatomic) id<BLControllerDelegate> loginViewControllerDelegate;
@property (weak, nonatomic) id<BLControllerDelegate> questionsViewControllerDelegate;

@end