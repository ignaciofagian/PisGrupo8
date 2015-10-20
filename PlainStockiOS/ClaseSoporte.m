//
//  ClaseSoporte.m
//  PlainStockiOS
//
//  Created by Mauricio Morinelli on 10/11/15.
//  Copyright © 2015 FING. All rights reserved.
//

#import "ClaseSoporte.h"
#import "BLController.h";

@implementation ClaseSoporte

-(void)operacion{
    BLController *b = [BLController getInstance];
    NSArray *a = b.cashHistory;
    
    NSLog(@"HOLA");
}
@end
