//
//  DateSystemViewController.h
//  PlainStockiOS
//
//  Created by nacho on 9/10/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol DateSystemViewControllerDelegate <NSObject>

@required
    - (void)dateValueFromDateSystemViewController:(NSString *)value;

@end



@interface MPSSystemDateViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIDatePicker *systemDatePicker;
@property (weak, nonatomic) NSString *systemDateString;
@property (weak, nonatomic) id<DateSystemViewControllerDelegate> delegate;

@end
