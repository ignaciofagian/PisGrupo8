//
//  DateSystemViewController.m
//  PlainStockiOS
//
//  Created by nacho on 9/10/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSSystemDateViewController.h"

@interface MPSSystemDateViewController ()

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
