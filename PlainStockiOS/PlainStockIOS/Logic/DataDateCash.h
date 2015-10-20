#import <Foundation/Foundation.h>

@interface DataDateCash : NSObject

@property (strong, nonatomic) NSString *date;
@property (strong, nonatomic) NSString *cash;

- (id)initWithDate:(NSString *)date cash:(NSString *)cash;

@end
