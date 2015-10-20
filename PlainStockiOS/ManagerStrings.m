#import "ManagerStrings.h"

@implementation ManagerStrings{
    
}

static ManagerStrings *instance = nil;


+ (ManagerStrings *)getInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (id)init {
    if (self = [super init]) {
        self.servidor = @"http://52.88.80.212:8080/Servidor/rest/app";
        self.preguntas_generales = @"preguntas/obtenerPreguntasGenerales";
        
    }
    return self;
}

@end
