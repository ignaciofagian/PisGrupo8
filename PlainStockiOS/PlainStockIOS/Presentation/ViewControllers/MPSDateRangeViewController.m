//
//  WYSettingsViewController.m
//
//  Created by Nicolas CHENG on 02/08/13.
//  Copyright (c) 2013 WHYERS. All rights reserved.
//

#import "MPSDateRangeViewController.h"
#import "MPSProgressViewController.h"

@interface MPSDateRangeViewController ()
{
}

@end

@implementation MPSDateRangeViewController {
    
    NSArray *chartRangesArray;
}

@synthesize tableView;

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    //For not showing anything below last row
    self.tableView.tableFooterView = [UIView new];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    if ([BLController sharedInstance].language == ENG)
        chartRangesArray = @[@"One day", @"One week", @"Two weeks", @"One month", @"Six months", @"One year", @"Five years", @"All"];
    else
        chartRangesArray = @[@"Un Día", @"Una semana", @"Dos semanas", @"Un mes", @"Seis meses", @"Un año", @"Cinco años", @"Todo"];
    
    [tableView reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)aTableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)aTableView numberOfRowsInSection:(NSInteger)section
{
    return chartRangesArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell* cell = [aTableView dequeueReusableCellWithIdentifier:@"WYSettingsTableViewCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"WYSettingsTableViewCell"];
    }
    
    cell.textLabel.text = chartRangesArray[indexPath.row];
    cell.textLabel.font = [UIFont fontWithName:@"Verdana" size:13];
    cell.textLabel.textAlignment = NSTextAlignmentCenter;
    return cell;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.delegate DateRangePopoverSelectedRange:indexPath.row];
}


@end
