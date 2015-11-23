#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "WSResponse.h"

typedef NS_ENUM(NSInteger, Language) {
    ENG,
    SPA,
};


@interface BLController : NSObject

+ (BLController  *)sharedInstance;

@property (nonatomic) Language language;
@property (strong, nonatomic) NSString *userId;
@property (nonatomic) BOOL isOnTimeMachineMode;
@property (strong, nonatomic) NSDate *timeMachineDate;
@property (nonatomic) double lastDatePersisted; //epoch (miliseconds)

-(void)setLanguage:(Language)language;
-(void)setUserId:(NSString *)userId;
-(void)setIsOnTimeMachineMode:(BOOL)isOnTimeMachineMode;
-(void)setTimeMachineDate:(NSDate *)timeMachineDate;

-(void)startCashHistoryUpdaterIfNeeded;
-(void)stopCashHistoryUpdater;

-(void)showConnectionErrorAlert;
-(void)showDBErrorAlert;

@end