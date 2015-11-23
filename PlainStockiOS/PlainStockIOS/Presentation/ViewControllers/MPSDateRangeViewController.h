//
//  WYSettingsViewController.h
//
//  Created by Nicolas CHENG on 02/08/13.
//  Copyright (c) 2013 WHYERS. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol DateRangePopoverDelegate <NSObject>
-(void) DateRangePopoverSelectedRange:(NSInteger)selectedRange;
@end


@interface MPSDateRangeViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) id<DateRangePopoverDelegate> delegate;
@property (nonatomic, weak) IBOutlet UITableView* tableView;

@end
