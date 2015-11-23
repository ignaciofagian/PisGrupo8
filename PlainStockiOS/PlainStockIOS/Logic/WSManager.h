#import <Foundation/Foundation.h>
#import "WSResponse.h"

@protocol WSManagerDelegate;


@interface WSManager : NSObject

+ (WSManager *)sharedInstance;

//Delegates
@property (weak, nonatomic) id<WSManagerDelegate> questionsDelegate;

// Sync WS
-(WSResponse *)registerUserWithID:(NSString *)userID;
-(WSResponse *)getGeneralQuestionsSynchronous;
-(WSResponse *)setGeneralAnswersSynchronous:(NSString *)answers;
-(WSResponse *)getSpecificQuestionsSynchronous;
-(WSResponse *)setSpecificAnswersSynchronous:(NSString *)answers;
-(WSResponse *)getAnswersSynchronous;
-(WSResponse *)resetQuestionsSynchronous;
-(WSResponse *)deleteUserSynchronous;
//-(WSResponse *)resetSimulationSynchronous;
-(WSResponse *)getCashHistorySynchronousFrom:(NSString *)from To:(NSString *)to;
-(WSResponse *)startTimeMachineModeSynchronous:(NSString *)date;
-(WSResponse *)timeJumpToDateSynchronous:(NSString *)date;

// Async WS
- (void) getCashHistoryFrom:(NSString *)fristDate to:(NSString *)lastDate;
- (void) getSpecificQuestions;
@end


//Protocol
@protocol WSManagerDelegate <NSObject>

@optional
-(void)servicioWeb:(NSString *)nombreServicio falloConError:(NSString *)error;
-(void)receivedDateCashHistoryResponse:(WSResponse *)response;
-(void)questionsResponseReceived:(NSArray *)response;
@end