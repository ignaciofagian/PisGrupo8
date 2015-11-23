//
//  MPSProgressViewController.h
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/14/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Charts/Charts.h>

@interface MPSProgressViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIButton *nextEventButton;
@property (weak, nonatomic) IBOutlet UIButton *dateJumpButton;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *emoticonImageView;


- (IBAction)showNextEventAlert:(id)sender;
- (IBAction)showTimeLeapDatePicker:(id)sender;

@end
