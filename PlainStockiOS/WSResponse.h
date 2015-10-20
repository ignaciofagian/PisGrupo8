#import <Foundation/Foundation.h>

@interface WSResponse : NSObject

@property (strong, nonatomic) NSString *error;
@property (strong, nonatomic) NSArray *data;




-(void)setData:(NSArray *)datos;
-(void)setError:(NSString *)err;

-(NSArray *)getData;
-(NSString *)getError;

@end
