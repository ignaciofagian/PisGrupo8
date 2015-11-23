//
//  BLController.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 10/3/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "BLController.h"
#import "DataQuestion.h"
#import "DataAnswer.h"
#import "DataDateCash.h"
#import <RestKit/RestKit.h>

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
    if (self = [super init]) {
        [self configureRestKit];
        self.userId = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        self.cashHistory = [self getCashHistory];
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
    RKObjectMapping *questionMapping = [RKObjectMapping mappingForClass:[DataQuestion class]];
    RKObjectMapping *answerMapping = [RKObjectMapping mappingForClass:[DataAnswer class]];
    [RKMIMETypeSerialization registerClass:[RKNSJSONSerialization class] forMIMEType:@"text/plain"];
    
    // set attribute mappings
    [questionMapping addAttributeMappingsFromDictionary:@{
                                                         @"id": @"idQuestion",
                                                         @"ing": @"textEng",
                                                         @"esp": @"textSpa",
                                                         }];
    
    [answerMapping addAttributeMappingsFromDictionary:@{
                                                        @"id": @"idAnswer",
                                                        @"ing": @"textEng",
                                                        @"esp": @"textSpa",
                                                        }];
    
    
    
    
    // link mapping with a relationship (for nested objects)
    RKRelationshipMapping *rel = [RKRelationshipMapping relationshipMappingFromKeyPath:@"resp" toKeyPath:@"answers" withMapping:answerMapping];
    [questionMapping addPropertyMapping:rel];
    

    
    
    // register mappings with the provider using a response descriptor
    RKResponseDescriptor *responseDescriptor =
            [RKResponseDescriptor responseDescriptorWithMapping:questionMapping
                                                         method:RKRequestMethodGET
                                                    pathPattern:@"preguntas/obtenerPreguntasGenerales"
                                                        keyPath:@""
                                                    statusCodes:[NSIndexSet indexSetWithIndex:200]];

    
    [objectManager addResponseDescriptor:responseDescriptor];
}


- (void) registerUserWithID:(NSString *)userID
{
    NSDictionary *queryParams = @{@"id" : userID};
    
    //TODO: esta llamada a WS hay que hacerla sincronica

    
    [[RKObjectManager sharedManager] getObjectsAtPath:@"registrar"
                                           parameters:queryParams
                                              success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                                  self.questions = mappingResult.array;
                                              }
                                              failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                                  NSLog(@"Error: No fue posible obtener las preguntas generales del servidor': %@", error);
                                              }];
}


- (NSArray *) getGeneralQuestions
{
        NSDictionary *queryParams = NULL;
    



//TODO: este WS hay que hacerlo sincronico o bloquear la pantalla de preguntas hasta recibir respuesta poniendo un activity indicator

//TODO: revisar porque las respuestas vienen con atributos null
    
    
        [[RKObjectManager sharedManager] getObjectsAtPath:@"preguntas/obtenerPreguntasGenerales"
                                               parameters:queryParams
                                                  success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                                      self.questions = mappingResult.array;
                                                      [self.questionsViewControllerDelegate questionsResponseReceived:_questions];
                                                  }
                                                  failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                                      NSLog(@"Error: No fue posible obtener las preguntas generales del servidor': %@", error);
                                                  }];
    
    //TODO: quitar de este ws y cualquier otro que tenga los retornos salvo que se logre hacer realmente sincronico,
    //      sino no sirve de nada que retorne, todo se hace en la llamada al delegado del 'success'
    //return self.questions;
    return NULL;
}


- (NSMutableArray *) getCashHistory
{
    return NULL;
}


- (void)dealloc {
    // Should never be called, but just here for clarity really.
}

@end