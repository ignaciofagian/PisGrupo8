//
//  DataAnswer.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/8/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "DataAnswer.h"

@implementation DataAnswer

- (id)initWithID:(NSString*)questionID textEng:(NSString *)englishText textSpa:(NSString *)spanishText answers:(DataAnswer*)answers selectedAnswer:(short)selectedAnswer
{
    self = [super init];
    if (self) {
        // Any custom setup work goes here
        _idAnswer = questionID;
        _textEng = englishText;
        _textSpa = spanishText;
    }
    return self;
}

@end
