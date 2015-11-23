#import <UIKit/UIKit.h>

@interface MPSConfigurationViewController : UITableViewController<UIPickerViewDelegate, UIPickerViewDataSource>

@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (weak, nonatomic) IBOutlet UILabel *receiveNotificationsLabel;
@property (weak, nonatomic) IBOutlet UILabel *dateTextLabel;
@property (weak, nonatomic) IBOutlet UILabel *systemDateLabel;
@property (weak, nonatomic) IBOutlet UIButton *changeDateButton;
@property (weak, nonatomic) IBOutlet UIButton *resetButton;
@property (weak, nonatomic) IBOutlet UIPickerView *languagePickerView;


- (IBAction)resetButtonPressed:(id)sender;
- (IBAction)resetInfoButtonPressed:(id)sender;

@end
