#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface DBManager : NSObject
{
    NSString *databasePath;
}

+(DBManager*)getSharedInstance;
-(BOOL)createDB;
-(BOOL) saveDate:(NSString*)date Saldo:(NSString*)saldo;
- (BOOL) saveDateCashPeriod:(NSArray *)datos;

-(NSArray*) findByRegisterNumber:(NSString*)registerNumber;
-(void)persistir:(NSString *)registro;
-(void)encontrar;


-(NSMutableArray *)readInformationFromDatabase;
-(NSMutableArray *)readInformationFrom:(NSString *)desde To:(NSString *)hasta;
@end