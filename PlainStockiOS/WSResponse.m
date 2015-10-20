#import "WSResponse.h"

@implementation WSResponse

-(void)setData:(NSArray *)datos
{
    _data = datos;
}

-(void)setError:(NSString *)err
{
    _error = err;
}

-(NSArray *)getData
{
    return _data;
}

-(NSString *)getError
{
    return _error;
}

@end
