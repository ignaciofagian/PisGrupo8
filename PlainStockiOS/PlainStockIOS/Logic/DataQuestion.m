//
//  DataPregunta.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/3/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "DataQuestion.h"
#import "DataAnswer.h"

@implementation DataQuestion


- (id)initWithID:(NSString*)questionID textEng:(NSString *)englishText textSpa:(NSString *)spanishText answers:(NSArray*)answers selectedAnswer:(short)selectedAnswer
{
    self = [super init];
    if (self) {
        // Any custom setup work goes here
        _idQuestion = questionID;
        _textEng = englishText;
        _textSpa = spanishText;
        _answers = answers;
        _selectedAnswer = selectedAnswer;
    }
    return self;
}


- (id)initWithID:(NSString*)questionID textEng:(NSString *)englishText textSpa:(NSString *)spanishText answers:(NSArray*)answers
{
    self = [super init];
    if (self) {
        // Any custom setup work goes here
        _idQuestion = questionID;
        _textEng = englishText;
        _textSpa = spanishText;
        _answers = answers;
        _selectedAnswer = -1;
    }
    return self;
}


@end
