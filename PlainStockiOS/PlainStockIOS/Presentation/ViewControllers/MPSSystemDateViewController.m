#import "MPSSystemDateViewController.h"
#import "BLController.h"
#import "WSManager.h"
#import "DBManager.h"
#import "AppDelegate.h"

NSString *const MIN_DATE_DEFAULT = @"01/01/1950";

@interface MPSSystemDateViewController () <UIAlertViewDelegate>

@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (weak, nonatomic) IBOutlet UILabel *warningLabel;
@property (weak, nonatomic) IBOutlet UIButton *doneButton;

@end

@implementation MPSSystemDateViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //setup date picker
    NSDictionary *dicConfig = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"]];
    NSString *minDateStr = [dicConfig objectForKey:@"MIN_DATE"] != nil ? [dicConfig objectForKey:@"MIN_DATE"] : MIN_DATE_DEFAULT;
    NSDate *minDate = [self dateFromString:minDateStr];
    NSTimeInterval oneDay = 60*60*24;
    NSDate *maxDate = [[NSDate date] dateByAddingTimeInterval: -1 * oneDay]; //yesterday is max date
    NSDate *currentDate;
    
    if ([BLController sharedInstance].isOnTimeMachineMode)
        currentDate = [BLController sharedInstance].timeMachineDate;
    else
        currentDate = maxDate;
    
    [self.systemDatePicker setDate:currentDate];
    [self.systemDatePicker setMinimumDate:minDate];
    [self.systemDatePicker setMaximumDate:maxDate];
}

- (void)viewWillAppear:(BOOL)animated
{
    Language lang = [BLController sharedInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"System Date" : @"Fecha del sistema";
    self.warningLabel.text = (lang == ENG) ? @"Warning: changing the system date will reset all your progress and start as the first time" : @"Atención: cambiar la fecha del sistema provocará que el mismo se reinicie y vuelva a comenzar como la primera vez";
    NSString *doneButtonTitle = (lang == ENG) ? @"Done" : @"Aceptar";
    [self.doneButton setTitle:doneButtonTitle forState:UIControlStateNormal];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (IBAction)btnAccept_touchDown:(id)sender {
    
    Language lang = [BLController sharedInstance].language;
    NSString *title = (lang == ENG) ? @"Warning" : @"Atención";
    NSString *cancel = (lang == ENG) ? @"Cancel" : @"Cancelar";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Your current progress will be erased and you will start on the selected date." : @"Su progreso actual será borrado y comenzará desde la fecha seleccionada.";
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:cancel
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:ok];
    [alert show];
}

-(void)startTimeMachineMode
{
    //Call start time machine web service and reset the user data
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *jumpDateStr = [dateFormatter stringFromDate:self.systemDatePicker.date];
    
    WSResponse *result = [[WSManager sharedInstance] startTimeMachineModeSynchronous:jumpDateStr];
    
    if (result.error == nil)
    {
        [[BLController sharedInstance] setIsOnTimeMachineMode:YES];
        [[BLController sharedInstance] setTimeMachineDate:self.systemDatePicker.date];
        
        if ([_delegate respondsToSelector:@selector(dateValueFromDateSystemViewController:)])
        {
            NSString *systemDateString = [self stringFromDate:self.systemDatePicker.date];
            [_delegate dateValueFromDateSystemViewController:systemDateString];
        }
        
        //Show the Login view
        AppDelegate *appDelegateTemp = [[UIApplication sharedApplication]delegate];
        [appDelegateTemp eraseDataAndRestartCurrentGame:YES];
        NSLog(@"Started time machine mode on date: %@", self.systemDatePicker.date);
    }
    else
    {
        [[BLController sharedInstance] showConnectionErrorAlert];
    }
}

-(NSString *)stringFromDate:(NSDate *)date
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"dd/MM/yyyy"];
    return [dateFormatter stringFromDate:date];
}

-(NSDate *)dateFromString:(NSString *)dateStr
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"dd/MM/yyyy"];
    return [dateFormatter dateFromString:dateStr];
}

#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1)
    {
        [self startTimeMachineMode];
    }
}


@end
