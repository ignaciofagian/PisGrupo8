#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <RestKit/RestKit.h>
#import "BLController.h"
#import "WSResponse.h"
#import "WSManager.h"
#import "DBManager.h"
#import "DataQuestion.h"
#import "DataAnswer.h"
#import "DataDateCash.h"


NSString *const WS_BASE_URL_DEFAULT = @"http://52.88.80.212:8080/Servidor/rest/app";

@implementation WSManager
{
    
}


static WSManager *instance = nil;

+ (WSManager *)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}


- (id)init {
    if (self = [super init])
    {
        [self configureRestKit];
    }
    return self;
}


- (void)configureRestKit
{
    //Disable RestKit logging
    RKLogConfigureByName("*", RKLogLevelOff);
    
    NSDictionary *dicConfig = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"]];
    NSString *baseURLStr = [dicConfig objectForKey:@"WS_BASE_URL"] != nil ? [dicConfig objectForKey:@"WS_BASE_URL"] : WS_BASE_URL_DEFAULT;
    
    // initialize AFNetworking HTTPClient
    NSURL *baseURL = [NSURL URLWithString:baseURLStr];
    AFHTTPClient *client = [[AFHTTPClient alloc] initWithBaseURL:baseURL];
    
    // initialize RestKit
    RKObjectManager *objectManager = [[RKObjectManager alloc] initWithHTTPClient:client];
    
    // create object mappings
    RKObjectMapping *dateCashMapping = [RKObjectMapping mappingForClass:[DataDateCash class]];
    RKObjectMapping *questionMapping = [RKObjectMapping mappingForClass:[DataQuestion class]];
    RKObjectMapping *answerMapping = [RKObjectMapping mappingForClass:[DataAnswer class]];
    [RKMIMETypeSerialization registerClass:[RKNSJSONSerialization class] forMIMEType:@"text/plain"];
    
    // set attribute mappings
    [dateCashMapping addAttributeMappingsFromDictionary:@{
                                                          @"tiempo": @"date",
                                                          @"saldo": @"cash",
                                                          @"l" : @"lost",
                                                          }];
    
    [questionMapping addAttributeMappingsFromDictionary:@{
                                                          @"id": @"questionId",
                                                          @"ing": @"textEng",
                                                          @"esp": @"textSpa",
                                                          @"idRespuestaUsuario": @"selectedAnswer",
                                                          }];
    
    [answerMapping addAttributeMappingsFromDictionary:@{
                                                        @"id": @"answerId",
                                                        @"ing": @"textEng",
                                                        @"esp": @"textSpa",
                                                        }];
    
    // link mapping with a relationship (for nested objects)
    RKRelationshipMapping *rel = [RKRelationshipMapping relationshipMappingFromKeyPath:@"resp" toKeyPath:@"answers" withMapping:answerMapping];
    [questionMapping addPropertyMapping:rel];
    
    
    RKResponseDescriptor *cashDescriptor =
    [RKResponseDescriptor responseDescriptorWithMapping:dateCashMapping
                                                 method:RKRequestMethodGET
                                            pathPattern:@"saldos"
                                                keyPath:@""
                                            statusCodes:[NSIndexSet indexSetWithIndex:200]];
    
    // register mappings with the provider using a response descriptor
    RKResponseDescriptor *questionDescriptor =
    [RKResponseDescriptor responseDescriptorWithMapping:questionMapping
                                                 method:RKRequestMethodGET
                                            pathPattern:@"preguntas/obtenerPreguntasEspecificas"
                                                keyPath:@""
                                            statusCodes:[NSIndexSet indexSetWithIndex:200]];
    
    // add the response descriptors to the object manager
    [objectManager addResponseDescriptor:cashDescriptor];
    [objectManager addResponseDescriptor:questionDescriptor];
    
    
    
    
}



#pragma mark - WS Synchronous

- (WSResponse *) registerUserWithID:(NSString *)userID
{
    NSString *URLPathComp = [NSString stringWithFormat:@"registrar?id=%@", userID];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];

    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];

    if (data == nil)
    {
        //you should always check that the return value is nil or NO before attempting to do anything with the NSError object.
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{ //this is (1)
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: Registered user with ID = %@", userID);
    }
    return responseWs;
}



-(WSResponse *)getGeneralQuestionsSynchronous
{
    NSString *URLPathComp = @"preguntas/obtenerPreguntasGenerales";
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];

    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        result = [self questionsArrayFromJSONArray:result];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: Got general questions: %lu questions", (unsigned long)result.count);
    }
    return responseWs;
    
}


-(WSResponse *)setGeneralAnswersSynchronous:(NSString *)answers
{
    //example: registrarRespuestasGenerales?id=1222&resp=2-3-4
    NSString *URLPathComp = [NSString stringWithFormat:@"preguntas/registrarRespuestasGenerales?id=%@&resp=%@", [BLController sharedInstance].userId, answers];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];

    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
 
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: Answers to general questions had been set");
    }
    return responseWs;
    
}


-(WSResponse *)getSpecificQuestionsSynchronous
{
    //example: obtenerPreguntasEspecificas?id=1222
    NSString *URLPathComp = [NSString stringWithFormat:@"preguntas/obtenerPreguntasEspecificas?id=%@", [BLController sharedInstance].userId];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];

    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        result = [self questionsArrayFromJSONArray:result];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: Got specific questions: %lu questions", (unsigned long)result.count);
    }
    return responseWs;
}


-(WSResponse *)setSpecificAnswersSynchronous:(NSString *)answers
{
    //example: registrarRespuestasEspecificas?id=1222&resp=5-7-8
    NSString *URLPathComp = [NSString stringWithFormat:@"preguntas/registrarRespuestasEspecificas?id=%@&resp=%@", [BLController sharedInstance].userId, answers];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];

    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: Answers to specific questions had been set");
    }
    return responseWs;
    
}



-(WSResponse*)getAnswersSynchronous
{
    //example: obtenerPreguntasRespondidas?id=887
    NSString *URLPathComp = [NSString stringWithFormat:@"preguntas/obtenerPreguntasRespondidas?id=%@", [BLController sharedInstance].userId];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];

    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];

    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        result = [self answeredQuestionsArrayFromJSONArray:result];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: Got answered questions: %lu questions", (unsigned long)result.count);
    }
    return responseWs;
}


-(WSResponse*)resetQuestionsSynchronous
{
    NSString *URLPathComp = [NSString stringWithFormat:@"preguntas/resetearTodasLasPreguntas?id=%@", [BLController sharedInstance].userId];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];
    
    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        [responseWs setData:result];
        [responseWs setError:nil];
        NSLog(@"WS_LOG: Reseted all questions");
    }
    return responseWs;
}


-(WSResponse*)deleteUserSynchronous
{
    NSString *URLPathComp = [NSString stringWithFormat:@"borrarCliente?id=%@", [BLController sharedInstance].userId];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];
    
    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        [responseWs setData:result];
        [responseWs setError:nil];
         NSLog(@"WS_LOG: User deleted from server");
    }
    return responseWs;
}

//
//-(WSResponse*)resetSimulationSynchronous
//{
//    NSString *URLPathComp = [NSString stringWithFormat:@"reset?id=%@", [BLController sharedInstance].userId];
//    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
//    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];
//    
//    WSResponse *responseWs = [[WSResponse alloc] init];
//    NSURLResponse * response;
//    NSArray* result = nil;
//    NSString *errorMsg;
//    NSError * error;
//    
//    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
//                                          returningResponse:&response
//                                                      error:&error];
//    
//    if (data == nil)
//    {
//        if (error != nil){
//            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
//                switch ([error code]) {
//                    case NSURLErrorCannotFindHost:
//                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
//                        break;
//                    case NSURLErrorCannotConnectToHost:
//                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
//                        break;
//                    case NSURLErrorNotConnectedToInternet:
//                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
//                        break;
//                    default:
//                        errorMsg = [error localizedDescription];
//                        break;
//                }
//                
//            } else {
//                errorMsg = [error localizedDescription];
//            }
//        }else{
//            errorMsg = @"Connection problem.";
//        }
//        
//        [responseWs setData:nil];
//        [responseWs setError:errorMsg];
//    }
//    else{
//        NSError *e;
//        result = [NSJSONSerialization JSONObjectWithData:data
//                                                 options:kNilOptions
//                                                   error:&e];
//        
//        [responseWs setData:result];
//        [responseWs setError:nil];
//        NSLog(@"WS_LOG: Reseted simulation.");
//    }
//    return responseWs;
//}


-(WSResponse*)getCashHistorySynchronousFrom:(NSString *)from To:(NSString *)to
{
    NSString *URLPathComp = [NSString stringWithFormat:@"saldos?id=%@&desde=%@&hasta=%@", [BLController sharedInstance].userId, from, to];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];
    
    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        result = [self dateCashArrayFromJSONArray:result];
        
        [responseWs setData:result];
        [responseWs setError:nil];
        NSLog(@"WS_LOG: Got cash history. Retrieved %ld records.", (unsigned long)responseWs.data.count);
    }
    return responseWs;
}


-(WSResponse *)startTimeMachineModeSynchronous:(NSString *)date
{
    NSString *URLPathComp = [NSString stringWithFormat:@"maquinatiempo?iden=%@&date=%@", [BLController sharedInstance].userId, date];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];
    
    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        [responseWs setData:result];
        [responseWs setError:nil];
        NSLog(@"WS_LOG: Started time machine mode.");
    }
    return responseWs;
}

-(WSResponse *)timeJumpToDateSynchronous:(NSString *)date
{
    NSString *URLPathComp = [NSString stringWithFormat:@"avanzar?id=%@&date=%@", [BLController sharedInstance].userId, date];
    NSURL *URL = [NSURL URLWithString:URLPathComp relativeToURL:[RKObjectManager sharedManager].baseURL];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:URL];
    
    WSResponse *responseWs = [[WSResponse alloc] init];
    NSURLResponse * response;
    NSArray* result = nil;
    NSString *errorMsg;
    NSError * error;
    
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    if (data == nil)
    {
        if (error != nil){
            if ([[error domain] isEqualToString:NSURLErrorDomain]) {
                switch ([error code]) {
                    case NSURLErrorCannotFindHost:
                        errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                        break;
                    case NSURLErrorCannotConnectToHost:
                        errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                        break;
                    case NSURLErrorNotConnectedToInternet:
                        errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                        break;
                    default:
                        errorMsg = [error localizedDescription];
                        break;
                }
                
            } else {
                errorMsg = [error localizedDescription];
            }
        }else{
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];
        [responseWs setError:errorMsg];
    }
    else{
        NSError *e;
        result = [NSJSONSerialization JSONObjectWithData:data
                                                 options:kNilOptions
                                                   error:&e];
        
        result = [self dateCashArrayFromJSONArray:result];

        if (result.count > 0)
        {
            int range = 0;
            
            for (int i = 0; i < result.count; i++)
            {
                range++;
                DataDateCash *ddc = result[i];
                if (ddc.lost == YES)
                    break;
            }
            
            if (range != result.count)
                result = [result subarrayWithRange: NSMakeRange(0, range)];
        }
        
        if (result.count > 0)
        {
            //Saves cash history to DB and sets last persisted date
            BOOL success = [[DBManager sharedInstance] saveDateCashPeriod:result];
            if (success)
                NSLog(@"Saved %d cash_history records to DB", result.count);
            else
                [[BLController sharedInstance] showDBErrorAlert];
        }
        
        [responseWs setData:result];
        [responseWs setError:nil];
        NSLog(@"WS_LOG: Jumped to date: %@", date);
    }
    return responseWs;
}

#pragma mark - WS Asynchronous

- (void) getCashHistoryFrom:(NSString *)firstDate to:(NSString *)lastDate
{
    NSDictionary *queryParams = @{
                                  @"id": [BLController sharedInstance].userId,
                                  @"desde": firstDate,
                                  @"hasta": lastDate
                                  };
    
    [[RKObjectManager sharedManager] getObjectsAtPath:@"saldos"
                                           parameters:queryParams
                                              success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                                  
                                                  
                                                  if (![BLController sharedInstance].isOnTimeMachineMode)
                                                  {
                                                      NSLog(@"Got cash history: %d records", mappingResult.array.count);
                                                      
                                                      NSArray *result = [mappingResult.array copy];
                                                      if (result.count > 0)
                                                      {
                                                          int range = 0;
                                                          
                                                          for (int i = 0; i < result.count; i++)
                                                          {
                                                              range++;
                                                              DataDateCash *ddc = result[i];
                                                              if (ddc.lost == YES)
                                                                  break;
                                                          }
                                                          
                                                          if (range != result.count)
                                                              result = [result subarrayWithRange: NSMakeRange(0, range)];
                                                      }
                                                      
                                                      if (result.count > 0)
                                                      {
                                                          //Saves cash history to DB and sets last persisted date
                                                          BOOL success = [[DBManager sharedInstance] saveDateCashPeriod:result];
                                                          if (success)
                                                              NSLog(@"Saved %d cash_history records to DB", result.count);
                                                          else
                                                              [[BLController sharedInstance] showDBErrorAlert];
                                                      }
                                                  }
                                                  
                                              }
                                              failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                                  //do nothing
                                                  NSLog(@"WS_ERROR: Couldn't get cash history from the server': %@", [error localizedDescription]);
                                                  
                                              }];
    
}

- (void) getSpecificQuestions
{
    NSDictionary *queryParams = nil;
    
    [[RKObjectManager sharedManager] getObjectsAtPath:@"preguntas/obtenerPreguntasEspecificas"
                                           parameters:queryParams
                                              success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                                  
                                                  NSLog(@"Got specific questions: %d questions", mappingResult.array.count);
                                                  
                                                  if ([self.questionsDelegate respondsToSelector:@selector(questionsResponseReceived:)])
                                                      [self.questionsDelegate questionsResponseReceived:mappingResult.array];
                                              }
                                              failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                                  //do nothing
                                                  NSLog(@"WS_ERROR: Couldn't get specific questions from the server': %@", [error localizedDescription]);
                                              }];
}


#pragma mark - Mappings

//Returns a NSMutableArray of DataQuestion from 'jsonArray'
- (NSMutableArray *) answeredQuestionsArrayFromJSONArray:(NSArray *)jsonArray
{
    NSMutableArray *questions = [[NSMutableArray alloc]init];
    
    for (int i = 0; i < jsonArray.count; i++)
    {
        NSDictionary *jsonQuestion = [jsonArray[i] objectForKey:@"pregunta"];
        DataQuestion *question = [[DataQuestion alloc]init];
        question.questionId = [[jsonQuestion objectForKey:@"id"] integerValue];
        question.textEng = [jsonQuestion objectForKey:@"ing"];
        question.textSpa = [jsonQuestion objectForKey:@"esp"];
        question.selectedAnswer = ([jsonArray[i] objectForKey:@"idRespuestaUsuario"] != nil) ? [[jsonArray[i] objectForKey:@"idRespuestaUsuario"] integerValue] : -1;
        question.answers = [self answersArrayFromJSONArray:[jsonQuestion objectForKey:@"resp"]];
        [questions addObject:question];
    }
    
    return questions;
}


//Returns a NSMutableArray of DataQuestion from 'jsonArray'
- (NSMutableArray *) questionsArrayFromJSONArray:(NSArray *)jsonArray
{
    NSMutableArray *questions = [[NSMutableArray alloc]init];
    
    for (int i = 0; i < jsonArray.count; i++)
    {
        NSDictionary *jsonQuestion = jsonArray[i];
        DataQuestion *question = [[DataQuestion alloc]init];
        question.questionId = [[jsonQuestion objectForKey:@"id"] integerValue];
        question.textEng = [jsonQuestion objectForKey:@"ing"];
        question.textSpa = [jsonQuestion objectForKey:@"esp"];
        question.selectedAnswer = ([jsonQuestion objectForKey:@"idRespuestaUsuario"] != nil) ? [[jsonQuestion objectForKey:@"idRespuestaUsuario"] integerValue] : -1;
        question.answers = [self answersArrayFromJSONArray:[jsonQuestion objectForKey:@"resp"]];
        [questions addObject:question];
    }
    
    return questions;
}


- (NSArray *) answersArrayFromJSONArray:(NSArray *)jsonArray
{
    NSMutableArray *answers = [[NSMutableArray alloc]init];
    
    for (int i = 0; i < jsonArray.count; i++)
    {
        NSDictionary *jsonAnswer = jsonArray[i];
        DataAnswer *answer = [[DataAnswer alloc]init];
        answer.answerId = [[jsonAnswer objectForKey:@"id"] integerValue];
        answer.textEng = [jsonAnswer objectForKey:@"ing"];
        answer.textSpa = [jsonAnswer objectForKey:@"esp"];
        [answers addObject:answer];
    }
    
    return [answers copy];
}



//Returns a NSArray of DataDateCash from 'jsonArray'
- (NSMutableArray *) dateCashArrayFromJSONArray:(NSArray *)jsonArray
{
    NSMutableArray *cashHistory = [[NSMutableArray alloc]init];
    
    for (int i = 0; i < jsonArray.count; i++)
    {
        NSDictionary *jsonAnswer = jsonArray[i];
        DataDateCash *dateCash = [[DataDateCash alloc]init];
        dateCash.date = [jsonAnswer objectForKey:@"tiempo"];
        dateCash.cash = [jsonAnswer objectForKey:@"saldo"];
        dateCash.lost = [[jsonAnswer objectForKey:@"l"] boolValue];
        [cashHistory addObject:dateCash];
    }
    
    return cashHistory;
}



@end





