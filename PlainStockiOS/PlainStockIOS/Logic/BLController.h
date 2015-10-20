#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "WSResponse.h"

typedef NS_ENUM(NSInteger, Language) {
    ENG,
    SPA,
};

@protocol BLControllerDelegate <NSObject>

//esto es el protocolo, estas operaciones la implementan los delegados, no las implementa BLController
@optional
-(void)receivedDateCashHistoryResponse:(WSResponse *)response;
@end



@interface BLController : NSObject{
    NSTimer *timer;
    bool isAppInBackground;
}

+ (BLController  *)getInstance;

@property (nonatomic) Language language;
@property (strong, nonatomic) NSString *userId;
@property (nonatomic) NSTimeInterval *systemDateOffset; //Seconds between system date and current date
@property (strong, nonatomic) NSMutableArray *questions;
@property (nonatomic) NSInteger numberOfAnsweredQuestions;
@property (strong, atomic) NSMutableArray *cashHistory;


/************** DELEGATES **************************************************************/
@property (weak, nonatomic) id<BLControllerDelegate> detailsDelegate;

@property (weak, nonatomic) id<BLControllerDelegate> progressDelegate;

/***************************************************************************************/

-(NSTimer *)getTimer;
-(bool)getIsAppInBackground;
-(void)setIsAppInBackground:(BOOL)value;

/********** Synchronous****************/
- (WSResponse *) registerUserWithID:(NSString *)userID;

-(WSResponse *)getGeneralQuestionsSynchronous;
-(WSResponse *)setGeneral:(NSString *)userId AnswersSynchronous:(NSString *)answers;

-(WSResponse *)getSpecificQuestionsSynchronous:(NSString *)userId;
-(WSResponse *)setSpecific:(NSString *)userId AnswersSynchronous:(NSString *)answers;


-(WSResponse*)getAnswersSynchronous:(NSString *)userId;

-(WSResponse*)resetQuestionsSynchronous:(NSString *)userId;
-(WSResponse*)deleteUserSynchronous:(NSString *)userId;
/**************************************/


/************* Asynchronous **************************/
- (void) getCashHistoryFrom:(NSString *)desde To:(NSString *)hasta Delegate:(id)delegate;
/****************************************************/


@end