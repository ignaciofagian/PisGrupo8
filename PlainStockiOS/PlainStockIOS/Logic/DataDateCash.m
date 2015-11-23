//
//  DataDateCash.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/3/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "DataDateCash.h"

@implementation DataDateCash

- (id)initWithDate:(NSDate *)date cash:(double*)cash
{
    self = [super init];
    if (self) {
        // Any custom setup work goes here
        _date = date;
        _cash = cash;
    }
    return self;
}

@end
