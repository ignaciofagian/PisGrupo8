//
//  ConfigurationTableViewController.h
//  PlainStockiOS
//
//  Created by nacho on 9/9/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MPSConfigurationViewController : UITableViewController<UIPickerViewDelegate, UIPickerViewDataSource>

@property (strong, nonatomic) NSArray *languageCollection;

@property (weak, nonatomic) IBOutlet UILabel *lblSystemDate;

@end
