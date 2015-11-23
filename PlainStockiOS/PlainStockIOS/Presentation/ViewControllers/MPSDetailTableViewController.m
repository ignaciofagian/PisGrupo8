//
//  DetailTableViewController.m
//  PlainStockiOS
//
//  Created by nacho on 9/6/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSDetailTableViewController.h"
#import "MPSDetailTableViewCell.h"
#import "BLController.h"

@interface MPSDetailTableViewController ()

@end


static NSString *cellIden = @"cellIden";

@implementation MPSDetailTableViewController

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    BLController *bl = [BLController getInstance];
    NSString *userId = bl.userId;
    
    
    NSString *urlService =
    @"http://52.88.80.212:8080/Servidor/rest/app/saldos?";
    urlService = [urlService stringByAppendingString:@"id="];
    urlService = [urlService stringByAppendingString:userId];
    urlService = [urlService stringByAppendingString:@"&desde=0&hasta=0"];

    
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:urlService]];
    NSURLResponse * response = nil;
    NSError * error = nil;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (error == nil)
    {
        // Parse data here
        NSArray* json = [NSJSONSerialization JSONObjectWithData:data
                                             options:kNilOptions
                                             error:&error];

        self.balanceHistory = [NSMutableArray array];
        
        int length = [json count];
        NSDictionary *firstBalance = [json objectAtIndex:0];
        NSNumber *balanceFirst = [firstBalance valueForKey:@"saldo"];
        long lastValue = [balanceFirst longValue];
        
        for (int indexValue = 0; indexValue< length; indexValue++)
        {
            
            NSDictionary *balance = [json objectAtIndex:indexValue];
            
            NSString *date = [NSString stringWithFormat:[balance valueForKey:@"tiempo"],indexValue];
            NSNumber *currentValue = [balance valueForKey:@"saldo"];
            
            long long_current_value = [currentValue longValue];
            NSString *saldo = [NSString stringWithFormat:@"%ld", long_current_value];
            
            long porcentaje = ((lastValue- long_current_value)/long_current_value)*100;
            NSString *porcentajeString = [NSString stringWithFormat:@"%ld", porcentaje];
            porcentajeString = [porcentajeString stringByAppendingString:@"%"];
            lastValue = long_current_value;
            
            date = [date substringToIndex:10]; //date is of format 2015/01/01, 10 characaters, time is removed
            
            
            NSDictionary *balance2 = @{
                                      @"date": date,
                                      @"percentage": porcentajeString,
                                      @"mount": saldo
                                      };
      
            [self.balanceHistory addObject:balance2];
            
        }
       
    }
   
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.balanceHistory count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    MPSDetailTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIden];
    if (cell == nil)
    {
        cell = [[MPSDetailTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIden];
    }
    
    [cell.lblDate setText:[[self.balanceHistory objectAtIndex:indexPath.row] objectForKey:@"date"]];
    [cell.lblPercentage setText:[[self.balanceHistory objectAtIndex:indexPath.row] objectForKey:@"percentage"]];
    cell.lblPercentage.textColor=[UIColor greenColor];
    [cell.lblMount setText:[[self.balanceHistory objectAtIndex:indexPath.row] objectForKey:@"mount"]];
    return cell;
    
}

@end
