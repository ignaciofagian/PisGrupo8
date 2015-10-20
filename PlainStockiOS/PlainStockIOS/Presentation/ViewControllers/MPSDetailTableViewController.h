//
//  DetailTableViewController.h
//  PlainStockiOS
//
//  Created by nacho on 9/6/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BLController.h"

@interface MPSDetailTableViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, BLControllerDelegate>

@property NSMutableArray  *balanceHistory;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *momentLabel;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;

@end
