//
//  MPSQuestionsViewController.h
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/29/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MPSNavBarMenuOptionButton.h"
#import "MPSAnswerButton.h"
#import "BLController.h"

@interface MPSQuestionsViewController : UIViewController <BLControllerDelegate, UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UIBarButtonItem *navBarMenuButton;
@property (weak, nonatomic) IBOutlet MPSNavBarMenuOptionButton *navBarMenuOptionButton;
@property (weak, nonatomic) IBOutlet UIButton *previousQuestionButton;
@property (weak, nonatomic) IBOutlet UIButton *nextQuestionButton;
@property (weak, nonatomic) IBOutlet UIButton *confirmAnswersButton;
@property (weak, nonatomic) IBOutlet UILabel *confirmAnswersLabel;
@property (weak, nonatomic) IBOutlet UILabel *questionLabel;
@property (weak, nonatomic) IBOutlet UITableView *tableView;


- (IBAction)navBarMenuButtonPressed:(id)sender;
- (IBAction)resetQuestionsButtonPressed:(id)sender;
- (IBAction)nextQuestionButtonPressed:(id)sender;
- (IBAction)previousQuestionButtonPressed:(id)sender;
- (IBAction)confirmAnswersButtonPressed:(id)sender;

@end
