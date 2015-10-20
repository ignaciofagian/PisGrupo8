//
//  DateSystemViewController.m
//  PlainStockiOS
//
//  Created by nacho on 9/10/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSSystemDateViewController.h"
#import "BLController.h"

@interface MPSSystemDateViewController ()

@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (weak, nonatomic) IBOutlet UILabel *warningLabel;
@property (weak, nonatomic) IBOutlet UIButton *doneButton;

@end

@implementation MPSSystemDateViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSDateFormatter *dateFormatter;
    dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"dd-MM-yyyy"];
    
    NSDate *date=[dateFormatter dateFromString:self.systemDateString];
    [self.systemDatePicker setDate:date];
}

- (void)viewWillAppear:(BOOL)animated
{
    Language lang = [BLController getInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"System Date" : @"Fecha del sistema";
    self.warningLabel.text = (lang == ENG) ? @"Warning: changing the system date will reset all your progress and start as the first time" : @"Atención: cambiar la fecha del sistema provocará que el mismo se reinicie y vuelva a comenzar como la primera vez";
    NSString *doneButtonTitle = (lang == ENG) ? @"Done" : @"Aceptar";
    [self.doneButton setTitle:doneButtonTitle forState:UIControlStateNormal];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (IBAction)btnAccept_touchDown:(id)sender {
    if ([_delegate respondsToSelector:@selector(dateValueFromDateSystemViewController:)])
    {
        
        NSDateFormatter *dateFormatter;
        dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"dd-MM-yyyy"];
        
        NSString *systemDateString = [dateFormatter stringFromDate:self.systemDatePicker.date];
        
        
        [_delegate dateValueFromDateSystemViewController:systemDateString];
    }
    
    [self.navigationController popViewControllerAnimated:YES];
}

@end
