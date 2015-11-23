#import <UIKit/UIKit.h>
#import "MPSNavBarMenuOptionButton.h"
#import "MPSAnswerButton.h"
#import "WSManager.h"

@interface MPSQuestionsViewController : UIViewController <WSManagerDelegate, UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) NSMutableArray *questions;
@property (nonatomic) NSInteger lastConfirmedAnswer;

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
