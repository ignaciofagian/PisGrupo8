#import <Foundation/Foundation.h>

@interface ManagerStrings : NSObject


+ (ManagerStrings  *)getInstance;

@property (strong, nonatomic) NSString *servidor;
@property (strong, nonatomic) NSString *preguntas_generales;


@end
