#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface DBManager : NSObject

@property (strong, nonatomic) NSString *databasePath;

+(DBManager*)sharedInstance;

-(BOOL)createDB;
-(BOOL)deleteDB;
-(BOOL)resetDB;
-(BOOL)saveDateCashPeriod:(NSArray *)data;
-(NSMutableArray *)getCashHistoryFrom:(double)from to:(double)to;
-(double)getLastPersistedDate;

@end