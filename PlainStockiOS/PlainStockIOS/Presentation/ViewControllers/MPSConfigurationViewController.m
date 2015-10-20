//
//  ConfigurationTableViewController.m
//  PlainStockiOS
//
//  Created by nacho on 9/9/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSConfigurationViewController.h"
#import "MPSSystemDateViewController.h"
#import "BLController.h"

@interface MPSConfigurationViewController ()<DateSystemViewControllerDelegate, UIPickerViewDelegate>

@property (strong, nonatomic) NSArray *languageCollection;
@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (weak, nonatomic) IBOutlet UILabel *receiveNotificationsLabel;
@property (weak, nonatomic) IBOutlet UILabel *dateTextLabel;
@property (weak, nonatomic) IBOutlet UILabel *systemDateLabel;
@property (weak, nonatomic) IBOutlet UIButton *changeDateButton;
@property (weak, nonatomic) IBOutlet UIButton *resetButton;
@property (weak, nonatomic) IBOutlet UIPickerView *languagePickerView;

@end

@implementation MPSConfigurationViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    _languageCollection = @[@"English", @"Español"];
    
    // Esta linea es para que no aparezcan mas lineas debajo del tableview
    self.tableView.tableFooterView = [UIView new];
}

- (void)viewWillAppear:(BOOL)animated
{
    Language lang = [BLController getInstance].language;
    
    if (lang == ENG)
        [self.languagePickerView selectRow:0 inComponent:0 animated:NO];
    else
        [self.languagePickerView selectRow:1 inComponent:0 animated:NO];
    
    self.navBarTitle.title = (lang == ENG) ? @"Settings" : @"Configuración";
    self.receiveNotificationsLabel.text = (lang == ENG) ? @"Receive notifications" : @"Recibir notificaciones";
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
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark - Table view data source, delegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 2)
    {
        return 2;
    }
    return 1;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    NSString *sectionName;
    Language lang = [BLController getInstance].language;
    
    switch (section)
    {
        case 0:
            sectionName = (lang == ENG) ? @"Notifications" : @"Notificaciones";
            break;
        case 1:
            sectionName = (lang == ENG) ? @"Language" : @"Lenguaje";
            break;
        case 2:
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
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    switch (row)
    {
        case 0:
            [BLController getInstance].language = ENG;
            [defaults setObject:@"ENG" forKey:@"language"];
            break;
        case 1:
            [BLController getInstance].language = SPA;
            [defaults setObject:@"SPA" forKey:@"language"];
            break;
    }
    
    [defaults synchronize];
    
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
        dateViewController.systemDateString = self.systemDateLabel.text;
    }
}





@end
