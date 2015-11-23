#import "MPSConfigurationViewController.h"
#import "MPSSystemDateViewController.h"
#import "BLController.h"
#import "DBManager.h"
#import "WSManager.h"
#import "AppDelegate.h"

@interface MPSConfigurationViewController ()<DateSystemViewControllerDelegate, UIPickerViewDelegate, UIAlertViewDelegate>

@property (strong, nonatomic) NSArray *languageCollection;

@end

@implementation MPSConfigurationViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationItem.backBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStylePlain target:nil action:nil];
    
    _languageCollection = @[@"English", @"Español"];
    
    //For not showing anything below last row
    self.tableView.tableFooterView = [UIView new];
}

- (void)viewWillAppear:(BOOL)animated
{
    //Language settings
    Language lang = [BLController sharedInstance].language;
    
    if (lang == ENG)
        [self.languagePickerView selectRow:0 inComponent:0 animated:NO];
    else
        [self.languagePickerView selectRow:1 inComponent:0 animated:NO];
    
    self.navBarTitle.title = (lang == ENG) ? @"Settings" : @"Configuración";
    self.dateTextLabel.text = (lang == ENG) ? @"Date" : @"Fecha";
    NSString *changeBtnTitle = (lang == ENG) ? @"Change" : @"Cambiar";
    NSString *resetButtonTitle = (lang == ENG) ? @"Reset" : @"Reiniciar";
    [self.changeDateButton setTitle:changeBtnTitle forState:UIControlStateNormal];
    [self.resetButton setTitle:resetButtonTitle forState:UIControlStateNormal];
    [self.tableView reloadData];
    
    [[self.tabBarController.tabBar.items objectAtIndex:0] setTitle:(lang == ENG) ? @"Questions" : @"Preguntas"];
    [[self.tabBarController.tabBar.items objectAtIndex:1] setTitle:(lang == ENG) ? @"Evolution" : @"Evolución"];
    [[self.tabBarController.tabBar.items objectAtIndex:2] setTitle:(lang == ENG) ? @"Details" : @"Detalles"];
    [[self.tabBarController.tabBar.items objectAtIndex:3] setTitle:(lang == ENG) ? @"Settings" : @"Config"];
    
    //setup date label
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"dd/MM/yyyy"];
    
    NSString *systemDateString;
    NSTimeInterval oneDay = 60*60*24;
    NSDate *maxDate = [[NSDate date] dateByAddingTimeInterval: -1 * oneDay]; //yesterday is max date
    
    if ([BLController sharedInstance].isOnTimeMachineMode)
        systemDateString = [dateFormatter stringFromDate:[BLController sharedInstance].timeMachineDate];
    else
        systemDateString = [dateFormatter stringFromDate:maxDate];

    
    self.systemDateLabel.text = systemDateString;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark - Table view data source, delegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 1)
    {
        return 2;
    }
    return 1;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    NSString *sectionName;
    Language lang = [BLController sharedInstance].language;
    
    switch (section)
    {
        case 0:
            sectionName = (lang == ENG) ? @"Language" : @"Lenguaje";
            break;
        case 1:
            sectionName = (lang == ENG) ? @"System" : @"Sistema";
            break;
    }
    return sectionName;
}



#pragma mark - Picker view delegate

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return _languageCollection.count;
}


- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    switch (row)
    {
        case 0:
            [[BLController sharedInstance] setLanguage:ENG];
            break;
        case 1:
            [[BLController sharedInstance] setLanguage:SPA];
            break;
    }
    
    [self viewWillAppear:NO];
}


- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view{
    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, pickerView.frame.size.width, 44)];
    label.backgroundColor = [UIColor clearColor];
    label.textColor = [UIColor blackColor];
    label.font = [UIFont fontWithName:@"Verdana" size:19];
    label.textAlignment = NSTextAlignmentCenter;
    label.text = _languageCollection[row];
    return label;    
}


#pragma mark - DateSystemViewController delegate

- (void)dateValueFromDateSystemViewController:(NSString *)value
{
    self.systemDateLabel.text = value;
    
}

// Este metodo sirve para identificar el momento en que se pasa desde la vista configuracion a la vista fecha sistema
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"segueSystemDate"])
    {
        MPSSystemDateViewController *dateViewController = segue.destinationViewController;
        dateViewController.delegate = self;
    }
}





- (IBAction)resetButtonPressed:(id)sender {
    
    
    Language lang = [BLController sharedInstance].language;
    NSString *title = (lang == ENG) ? @"Message" : @"Mensaje";
    NSString *cancel = (lang == ENG) ? @"Cancel" : @"Cancelar";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Are you sure you want to reset the simulation?" : @"¿Seguro que desea resetear la simulación?";
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:cancel
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:ok];
    alert.tag = 0;
    [alert show];
}



- (IBAction)resetInfoButtonPressed:(id)sender {
    
    Language lang = [BLController sharedInstance].language;
    NSString *title = (lang == ENG) ? @"Message" : @"Mensaje";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Resetting the simulation erases all your current progress and starts again from scratch." : @"Resetear la simulación borra todo su progreso y luego comienza de cero.";
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:ok
                                           otherButtonTitles: nil];
    alert.tag = 2;
    [alert show];
}


-(void)resetSimulationOnRealTime
{
    WSResponse *result = [[WSManager sharedInstance] deleteUserSynchronous];
    
    if (result.error != nil)
    {
        NSLog(@"Error on resetSimulation: %@", result.error);
        [[BLController sharedInstance] showConnectionErrorAlert];
    }
    else
    {
        //Show the Login view
        AppDelegate *appDelegateTemp = [[UIApplication sharedApplication]delegate];
        [appDelegateTemp eraseDataAndRestartCurrentGame:NO];
        NSLog(@"Simulation resetted successfully.");
    }
}




#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 0) //Reset confirmation alert
    {
        if (buttonIndex == 1)
        {
            [self resetSimulationOnRealTime];
        }
    }
}



@end
