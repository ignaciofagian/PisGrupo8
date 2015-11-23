#import <UIKit/UIKit.h>
#import "WSManager.h"

@interface MPSLoginViewController : UIViewController <WSManagerDelegate>


- (IBAction)startButtonPressed:(id)sender;


@property (weak, nonatomic) IBOutlet UILabel *bodyLabel;
@property (weak, nonatomic) IBOutlet UIButton *startButton;


@end

