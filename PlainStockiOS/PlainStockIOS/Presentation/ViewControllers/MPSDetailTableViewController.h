//
//  DetailTableViewController.h
//  PlainStockiOS
//
//  Created by nacho on 9/6/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MPSDetailTableViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property NSMutableArray  *balanceHistory;
//@property NSMutableData  *data;

@end
