#import <UIKit/UIKit.h>
#import <Charts/Charts.h>
#import "WSManager.h"
#import "BLController.h"

typedef NS_ENUM(NSInteger, DateRange) {
    oneDay,
    oneWeek,
    twoWeeks,
    oneMonth,
    sixMonths,
    oneYear,
    fiveYears,
    All,
};

@interface MPSProgressViewController : UIViewController <WSManagerDelegate>

@property (weak, nonatomic) IBOutlet LineChartView *chartView;
@property (weak, nonatomic) IBOutlet UINavigationBar *navigationBar;
@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *navBarTopConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *botConstraint;
@property (weak, nonatomic) IBOutlet UILabel *rangeLabel;
@property (weak, nonatomic) IBOutlet UIButton *dateJumpButton;
@property (weak, nonatomic) IBOutlet UILabel *goToNextDateLabel;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *emoticonImageView;
@property (weak, nonatomic) IBOutlet UITextField *auxDateTextField;
@property (nonatomic, weak) IBOutlet UIButton* popoverButton;


- (IBAction)showTimeLeapDatePicker:(id)sender;
- (IBAction)popoverBtnPressed:(id)sender;

@end
