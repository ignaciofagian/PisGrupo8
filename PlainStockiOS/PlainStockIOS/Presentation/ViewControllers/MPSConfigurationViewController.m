//
//  ConfigurationTableViewController.m
//  PlainStockiOS
//
//  Created by nacho on 9/9/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSConfigurationViewController.h"
#import "MPSSystemDateViewController.h"

@interface MPSConfigurationViewController ()<DateSystemViewControllerDelegate, UIPickerViewDelegate>


@end

@implementation MPSConfigurationViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    _languageCollection = @[@"Español", @"Ingles"];
    
    // Esta linea es para que no aparezcan mas lineas debajo del tableview
    self.tableView.tableFooterView = [UIView new];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark - Table view data source

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





#pragma mark - Picker view delegate

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return _languageCollection.count;
}


//- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row
//            forComponent:(NSInteger)component
//{
//    return _languageCollection[row];
//}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view{
    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, pickerView.frame.size.width, 44)];
    label.backgroundColor = [UIColor clearColor];
    label.textColor = [UIColor blackColor];
    label.font = [UIFont fontWithName:@"Verdana" size:19];
    //label.text = [NSString stringWithFormat:@" %d", row+1];
    label.textAlignment = NSTextAlignmentCenter;
    label.text = _languageCollection[row];
    return label;    
}


#pragma mark - DateSystemViewController delegate

- (void)dateValueFromDateSystemViewController:(NSString *)value
{
    self.lblSystemDate.text = value;
    
}

// Este metodo sirve para identificar el momento en que se pasa desde la vista configuracion a la vista fecha sistema
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"segueSystemDate"])
    {
        MPSSystemDateViewController *dateViewController = segue.destinationViewController;
        dateViewController.delegate = self;
        dateViewController.systemDateString = self.lblSystemDate.text;
    }
}





@end
