//
//  MPSProgressViewController.h
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/14/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Charts/Charts.h>
#import "BLController.h"

@interface MPSProgressViewController : UIViewController <BLControllerDelegate>

@property (weak, nonatomic) IBOutlet LineChartView *chartView;
@property (weak, nonatomic) IBOutlet UINavigationBar *navigationBar;
@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *navBarTopConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *botConstraint;
@property (weak, nonatomic) IBOutlet UIButton *nextEventButton;
@property (weak, nonatomic) IBOutlet UIButton *dateJumpButton;
@property (weak, nonatomic) IBOutlet UILabel *goToNextEventLabel;
@property (weak, nonatomic) IBOutlet UILabel *goToNextDateLabel;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *emoticonImageView;
@property (weak, nonatomic) IBOutlet UITextField *auxDateTextField;



- (IBAction)showNextEventAlert:(id)sender;
- (IBAction)showTimeLeapDatePicker:(id)sender;

@end
