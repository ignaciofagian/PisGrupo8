#import <UIKit/UIKit.h>


@protocol DateSystemViewControllerDelegate <NSObject>

@required
- (void)dateValueFromDateSystemViewController:(NSString *)value;

@end



@interface MPSSystemDateViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIDatePicker *systemDatePicker;
@property (weak, nonatomic) id<DateSystemViewControllerDelegate> delegate;

@end
