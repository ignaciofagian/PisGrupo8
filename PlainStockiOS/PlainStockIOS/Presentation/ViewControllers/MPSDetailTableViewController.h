#import <UIKit/UIKit.h>
#import "WSManager.h"

@interface MPSDetailTableViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, WSManagerDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *momentLabel;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;

@end
