//
//  DetailTableViewCell.h
//  PlainStockiOS
//
//  Created by nacho on 9/11/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MPSDetailTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblPercentage;
@property (weak, nonatomic) IBOutlet UILabel *lblMount;

@end
