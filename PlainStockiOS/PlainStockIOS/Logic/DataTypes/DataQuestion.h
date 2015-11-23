#import <Foundation/Foundation.h>
#import "DataAnswer.h"

@interface DataQuestion : NSObject

@property (nonatomic) NSInteger questionId;
@property (strong, nonatomic) NSString *textEng;
@property (strong, nonatomic) NSString *textSpa;
@property (strong, nonatomic) NSArray *answers;
@property (nonatomic) NSInteger selectedAnswer;

@end
