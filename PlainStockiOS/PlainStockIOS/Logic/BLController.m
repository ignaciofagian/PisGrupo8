#import "BLController.h"
#import "DataQuestion.h"
#import "DataAnswer.h"
#import "DataDateCash.h"
#import <RestKit/RestKit.h>
#import "MPSDetailTableViewController.h"
#import "WSResponse.h"

NSString *const WS_BASE_URL = @"http://52.88.80.212:8080/Servidor/rest/app";

@implementation BLController{
    
}

static BLController *instance = nil;


+ (BLController *)getInstance
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
        self.language = ENG;
        self.numberOfAnsweredQuestions = 0;
        self.systemDateOffset = 0;
        self.userId = NULL;
        self.questions = NULL;
        self.cashHistory = NULL;
        
        [self configureRestKit];
        self->isAppInBackground = false;
        [self startTimedTask];
    }
    return self;
}


- (void)configureRestKit
{
    // initialize AFNetworking HTTPClient
    NSURL *baseURL = [NSURL URLWithString:@"http://52.88.80.212:8080/Servidor/rest/app"];
    AFHTTPClient *client = [[AFHTTPClient alloc] initWithBaseURL:baseURL];
    
    // initialize RestKit
    RKObjectManager *objectManager = [[RKObjectManager alloc] initWithHTTPClient:client];
    
    // create object mappings
    /*RKObjectMapping *questionMapping = [RKObjectMapping mappingForClass:[DataQuestion class]];
    RKObjectMapping *answerMapping = [RKObjectMapping mappingForClass:[DataAnswer class]];*/
    RKObjectMapping *dateCashMapping = [RKObjectMapping mappingForClass:[DataDateCash class]];
    [RKMIMETypeSerialization registerClass:[RKNSJSONSerialization class] forMIMEType:@"text/plain"];
   
    
    // set attribute mappings
    /*[questionMapping addAttributeMappingsFromDictionary:@{
                                                          @"id": @"idQuestion",
                                                          @"ing": @"textEng",
                                                          @"esp": @"textSpa",
                                                          }];
    
    [answerMapping addAttributeMappingsFromDictionary:@{
                                                        @"id": @"idAnswer",
                                                        @"ing": @"textEng",
                                                        @"esp": @"textSpa",
                                                        }];*/
    
    [dateCashMapping addAttributeMappingsFromDictionary:@{
                                                          @"tiempo": @"date",
                                                          @"saldo": @"cash",
                                                          }];
    
    
    // link mapping with a relationship (for nested objects)
   /* RKRelationshipMapping *rel = [RKRelationshipMapping relationshipMappingFromKeyPath:@"resp" toKeyPath:@"answers" withMapping:answerMapping];
    [questionMapping addPropertyMapping:rel];
    
    
    
    
    // register mappings with the provider using a response descriptor
    RKResponseDescriptor *questionDescriptor =
    [RKResponseDescriptor responseDescriptorWithMapping:questionMapping
                                                 method:RKRequestMethodGET
                                            pathPattern:@"preguntas/obtenerPreguntasGenerales"
                                                keyPath:@""
                                            statusCodes:[NSIndexSet indexSetWithIndex:200]];
    
    [objectManager addResponseDescriptor:questionDescriptor];*/
    
    RKResponseDescriptor *cashDescriptor =
    [RKResponseDescriptor responseDescriptorWithMapping:dateCashMapping
                                                 method:RKRequestMethodGET
                                            pathPattern:@"saldos2"
                                                keyPath:@""
                                            statusCodes:[NSIndexSet indexSetWithIndex:200]];
    
    [objectManager addResponseDescriptor:cashDescriptor];
}



#pragma mark - WS Synchronous
/********************************************************** SINCRONICOS *******************************************************************/

//TODO: userId se puede quitar de todos los WS dado que BLController lo tiene.
//      Salvo que se decida pasar los ws a una clase especializada en eso.

- (WSResponse *) registerUserWithID:(NSString *)userID
{
    
    NSArray* respuesta = nil; //esta es la respuesta del servidor convertida a NSArray
    WSResponse *responseWs = [[WSResponse alloc] init];  //esta es la respuesta de la operacion, tiene el NSArray y el Error si existe uno
    
    
    NSString *URL = @"http://52.88.80.212:8080/Servidor/rest/app/registrar?id=";
    URL = [URL stringByAppendingString:userID];
    // Send a synchronous request
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    /* Return Value
     (1) The downloaded data for the URL request. Returns nil if a connection could not be created or if the download fails.
     */
    
    NSString *errorMsg;
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
        }else{ //esto es (1)
            errorMsg = @"Connection problem.";
        }
        
        [responseWs setData:nil];       //como hubo error, no devuelvo data
        [responseWs setError:errorMsg];
    }
    else{
        //tuvo exito, recibio data
        NSError *e;
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                        options:kNilOptions
                                                          error:&e];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Registered user with ID = %@", userID);
    }
    return responseWs;
}



-(WSResponse *)getGeneralQuestionsSynchronous
{
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    
    NSString *URL = @"http://52.88.80.212:8080/Servidor/rest/app/preguntas/obtenerPreguntasGenerales";
    // Send a synchronous request
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        respuesta = [self questionsArrayFromJSONArray:respuesta];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Got general questions: %lu questions", (unsigned long)respuesta.count);
    }
    return responseWs;
    
}


-(WSResponse *)setGeneral:(NSString *)userId AnswersSynchronous:(NSString *)answers
{
    //52.88.80.212:8080/Servidor/rest/app/preguntas/registrarRespuestasGenerales?id=1222&resp=2-3-4
    
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    
    NSString *URL = @"http://52.88.80.212:8080/Servidor/rest/app/preguntas/registrarRespuestasGenerales?id=";
    // Send a synchronous request
    URL = [URL stringByAppendingString:userId];
    URL = [URL stringByAppendingString:@"&resp="];
    URL = [URL stringByAppendingString:answers];
    
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Answers to general questions had been set");
    }
    return responseWs;
    
}


-(WSResponse *)getSpecificQuestionsSynchronous:(NSString *)userId
{
    //52.88.80.212:8080/Servidor/rest/app/preguntas/obtenerPreguntasEspecificas?id=1222
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    
    NSString *URL = @"http://52.88.80.212:8080/Servidor/rest/app/preguntas/obtenerPreguntasEspecificas?id=";
    // Send a synchronous request
    URL = [URL stringByAppendingString:userId];
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        respuesta = [self questionsArrayFromJSONArray:respuesta];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Got specific questions: %lu questions", (unsigned long)respuesta.count);
    }
    return responseWs;
}


-(WSResponse *)setSpecific:(NSString *)userId AnswersSynchronous:(NSString *)answers
{
    //52.88.80.212:8080/Servidor/rest/app/preguntas/registrarRespuestasEspecificas?id=1222&resp=5-7-8
    
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    
    NSString *URL = @"http://52.88.80.212:8080/Servidor/rest/app/preguntas/registrarRespuestasEspecificas?id=";
    // Send a synchronous request
    URL = [URL stringByAppendingString:userId];
    URL = [URL stringByAppendingString:@"&resp="];
    URL = [URL stringByAppendingString:answers];
    
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Answers to specific questions had been set");
    }
    return responseWs;
    
}



-(WSResponse*)getAnswersSynchronous:(NSString *)userId
{
    //52.88.80.212:8080/Servidor/rest/app/preguntas/obtenerPreguntasRespondidas?id=887
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    
    NSString *URL = @"http://52.88.80.212:8080/Servidor/rest/app/preguntas/obtenerPreguntasRespondidas?id=";
    // Send a synchronous request
    URL = [URL stringByAppendingString:userId];

    
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        respuesta = [self answeredQuestionsArrayFromJSONArray:respuesta];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Got answered questions: %lu questions", (unsigned long)respuesta.count);
    }
    return responseWs;
}


-(WSResponse*)resetQuestionsSynchronous:(NSString *)userId
{
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    NSString *URL = WS_BASE_URL;
    URL = [URL stringByAppendingString:@"/preguntas/resetearTodasLasPreguntas?id="];
    URL = [URL stringByAppendingString:userId];
    
    // Send a synchronous request
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"Reseted general questions");
    }
    return responseWs;
}


-(WSResponse*)deleteUserSynchronous:(NSString *)userId
{
    NSArray* respuesta = nil;
    WSResponse *responseWs = [[WSResponse alloc] init];
    
    NSString *URL = WS_BASE_URL;
    URL = [URL stringByAppendingString:@"borrarCliente?id="];
    URL = [URL stringByAppendingString:userId];
    
    // Send a synchronous request
    NSURLRequest * urlRequest = [NSURLRequest requestWithURL:
                                 [NSURL URLWithString:URL]];
    
    NSURLResponse * response;
    NSError * error;
    NSData * data = [NSURLConnection sendSynchronousRequest:urlRequest
                                          returningResponse:&response
                                                      error:&error];
    
    NSString *errorMsg;
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
        respuesta = [NSJSONSerialization JSONObjectWithData:data
                                                    options:kNilOptions
                                                      error:&e];
        
        [responseWs setData:respuesta];
        [responseWs setError:nil];
        NSLog(@"User deleted from server");
    }
    return responseWs;
}







#pragma mark - WS Asynchronous
/****************************** ASYNCHRONOUS ************************************************************/
//TODO MAURI: OJO QUE GUILLERMO LE PUSO SALDOS2 EN VEZ DE SALDOS, NO SE PORQUE
//TODO MAURI: CONTROLAR EL TEMA DE LAS FECHAS, PORQUE CUANDO PEDIS EL SERVICIO LO HACES SIN HORAS Y MINUTOS
//TODO MAURI: PERO CUANDO CONTESTA CONTESTA CON HORAS Y MINUTOS, BRUTO PEDO
- (void) getCashHistoryFrom:(NSString *)desde To:(NSString *)hasta Delegate:(id)delegate
{//TODO: PASAR DELEGADO EN EL LLAMADO
    NSDictionary *queryParams = @{
                                  @"id": self.userId,
                                  @"desde": desde,
                                  @"hasta": hasta
                                  };
    
    [[RKObjectManager sharedManager] getObjectsAtPath:@"saldos2"
                                           parameters:queryParams
                                              success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                                  self.cashHistory = [mappingResult.array mutableCopy];
                                                  WSResponse *response = [[WSResponse alloc]init];
                                                  [response setError:nil];
                                                  [response setData:self.cashHistory];
                                                  
                                                  [delegate receivedDateCashHistoryResponse:response];
                                                  [delegate receivedDateCashHistoryResponse:response]; //TODO: BORRAR, se le pide a db, aca solo se guarda en bd
                                                  NSLog(@"Got cash history: %lu records", (unsigned long)self.cashHistory.count);
                                              }
                                              failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                                  //quizas hay que delegar para que imprima una alert en la pantalla
                                                  //que corresponde
                                                  NSString *errorMsg;
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
                                                  WSResponse *response = [[WSResponse alloc]init];
                                                  [response setError:errorMsg];
                                                  [response setData:nil];
                                                  
                                                  [self.detailsDelegate receivedDateCashHistoryResponse:response];
                                              }];
    
}
/*****************************************************************************************************************************/
//cada vez que el timer alcanza 5 segundos, ejecuta la tarea, auqneu haya otra ejecutandose, por eso
//es que veo que antes de llegar a 100 arranca a contar de nuevo
//parece que puedo crear muchos tareas a la vez, hay poco orden ahi

//cuando voy a background con la app, estas tareas se siguen ejecutando, deberiamos pararlas
// y reiniciarlas cuando se vuelve a foreground

//otra cosa que no vi nada y que va a estar dificil es el manejo de errores
// que pasa si hay un error y el thread no se ejecuta nunca mas
//hay que ver algun mecanismo para asegurarnos que el timer siempre ejecuta la tarea que queremos y que no quede colgado
//por alguna razon


-(NSTimer *)getTimer
{
    return self->timer;
}

-(bool)getIsAppInBackground
{
    return self->isAppInBackground;
}

-(void)setIsAppInBackground:(BOOL)value
{
	self->isAppInBackground = value;
}

- (void)startTimedTask
{
    self->timer = [NSTimer scheduledTimerWithTimeInterval:120 target:self selector:@selector(performBackgroundTask) userInfo:nil repeats:YES];
}

- (void)performBackgroundTask
{

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        if (!self->isAppInBackground)
        {
                //Do background work
            [self tarea];
            [NSThread sleepForTimeInterval:5];
            for (int i = 0; i < 1000; i++) {
                [NSThread sleepForTimeInterval:.5];
                NSLog(@"Soy delay");
            }
                NSLog(@"hola blcontroller");
        
                dispatch_async(dispatch_get_main_queue(), ^{
                    //Update UI
                    NSLog(@"ACTUALIZA UI");
                });
        }
    });        
}

-(void)tarea
{
    [NSThread sleepForTimeInterval:5000];
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
        question.selectedAnswer = ([jsonArray[i] objectForKey:@"idRespuestaUsuario"] != NULL) ? [[jsonArray[i] objectForKey:@"idRespuestaUsuario"] integerValue] : -1;
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
        question.selectedAnswer = ([jsonQuestion objectForKey:@"idRespuestaUsuario"] != NULL) ? [[jsonQuestion objectForKey:@"idRespuestaUsuario"] integerValue] : -1;
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
        dateCash.date = [jsonAnswer objectForKey:@"tiemo"];
        dateCash.cash = [jsonAnswer objectForKey:@"saldo"];
        [cashHistory addObject:dateCash];
    }
    
    return cashHistory;
}




- (void)dealloc {
    // Should never be called, but just here for clarity really.
}

@end