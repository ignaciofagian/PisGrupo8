#import <Foundation/Foundation.h>

@interface DataDateCash : NSObject

@property (strong, nonatomic) NSString *date;
@property (strong, nonatomic) NSString *cash;
@property (nonatomic) BOOL lost;

- (id)initWithDate:(NSString *)date cash:(NSString *)cash lost:(BOOL)lost;

@end
