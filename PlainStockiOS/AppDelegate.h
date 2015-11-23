#import <UIKit/UIKit.h>
//
//typedef NS_ENUM(int, TabBarView) {
//    QuestionViewController,
//    ProgressViewController,
//    DetailsViewController,
//    SettingsViewController
//};
//

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property BOOL restrictRotation;


- (void)showQuestionsViewController:(BOOL)isReset;
- (void)showLoginView;
-(BOOL)eraseDataAndRestartCurrentGame:(BOOL)isTimeMahineMode;

@end

