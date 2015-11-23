#import "DataDateCash.h"

@implementation DataDateCash

- (id)initWithDate:(NSString *)date cash:(NSString *)cash lost:(BOOL)lost
{
    self = [super init];
    if (self) {
        // Any custom setup work goes here
        _date = date;
        _cash = cash;
        _lost = lost;
    }
    return self;
}

@end
