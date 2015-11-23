//
//  MPSQuestionsViewController.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/29/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSQuestionsViewController.h"
#import "BLController.h"
#import "DataQuestion.h"

#define BG_COLOR_NORMAL [UIColor colorWithRed:239.0/255.0 green:239.0/255.0 blue:244.0/255.0 alpha:1.0]
#define BG_COLOR_HIGHLIGHTED [UIColor colorWithRed:0.0 green:128.0/255.0 blue:1.0 alpha:1.0]
#define TITLE_COLOR_NORMAL [UIColor colorWithRed:0.0 green:128.0/255.0 blue:1.0 alpha:1.0]
#define TITLE_COLOR_HIGHLIGHTED [UIColor whiteColor]


@interface MPSQuestionsViewController () <UIAlertViewDelegate>

@property (strong, nonatomic) NSArray *questions;
@property (strong, nonatomic) NSMutableArray *answers;
@property (strong, nonatomic) NSMutableArray *answerButtons;
@property (nonatomic) NSInteger currentQuestion;
@property (nonatomic) NSInteger lastAnsweredQuestion;

@end

@implementation MPSQuestionsViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupButtons];
    
    self.lastAnsweredQuestion = [self getLastAnsweredQuestion];
    self.currentQuestion = self.lastAnsweredQuestion;
    [BLController getInstance].questionsViewControllerDelegate = self;
    self.questions = [NSArray arrayWithArray:[[BLController getInstance] getGeneralQuestions]];
    self.answers = [[NSMutableArray alloc] initWithCapacity:self.questions.count];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


#pragma mark - Actions

- (IBAction)navBarMenuButtonPressed:(id)sender
{
    self.navBarMenuOptionButton.hidden = !self.navBarMenuOptionButton.hidden;
    self.navBarMenuOptionButton.enabled = !self.navBarMenuOptionButton.enabled;
}


- (IBAction)resetQuestionsButtonPressed:(id)sender
{
    self.navBarMenuOptionButton.hidden = YES;
    self.navBarMenuOptionButton.enabled = NO;
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:@"Mensaje"
                                                     message:@"Seguro que desea resetear las preguntas?"
                                                    delegate:self
                                           cancelButtonTitle:@"Cancelar"
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:@"Aceptar"];
    [alert show];
}

//TODO: hacer todo con el mismo metodo chequeando que boton se apreta

- (IBAction)yesButtonPressed:(id)sender
{
    self.yesButton.selected = YES;
    self.maybeButton.selected = NO;
    self.noButton.selected = NO;
    
    //[self selectAnswerButtonAtIndex:btnIndex];
    
    //[self.answers setObject:@"yes" atIndexedSubscript:self.currentQuestion];
    
    //Si todavia no contesto la pregunta
    if (self.currentQuestion >= self.lastAnsweredQuestion)
    {
        self.lastAnsweredQuestion = self.currentQuestion;
        
        if (self.currentQuestion == self.questions.count - 1)
        {
            [self nextButtonHidden:YES];
            [self confirmButtonHidden:NO];
        }
        else
        {
            [self nextButtonHidden:NO];
        }
    }
}


- (IBAction)maybeButtonPressed:(id)sender
{
    self.yesButton.selected = NO;
    self.maybeButton.selected = YES;
    self.noButton.selected = NO;
}


- (IBAction)noButtonPressed:(id)sender
{
    self.yesButton.selected = NO;
    self.maybeButton.selected = NO;
    self.noButton.selected = YES;
}


- (IBAction)nextQuestionButtonPressed:(id)sender
{
    self.currentQuestion++;
    self.questionLabel.text = [self.questions[self.currentQuestion] textEng];
    
    //Si todavia no contesto la pregunta
    if (self.currentQuestion > self.lastAnsweredQuestion)
    {
        [self nextButtonHidden:YES];
        [self confirmButtonHidden:YES];
        [self deselectAnswerButtons];
    }
    else
    {
        //[self selectAnswerButtonAtIndex:btnIndex]; //TODO: implementar aca y descomentar en varios lugares
        
        //Si es la ultima pregunta
        if (self.currentQuestion == self.questions.count - 1)
        {
            [self nextButtonHidden:YES];
            [self confirmButtonHidden:NO];
        }
        else
        {
            [self nextButtonHidden:NO];
            [self confirmButtonHidden:YES];
        }
    }
    
    [self previousButtonHidden:NO];
}


- (IBAction)previousQuestionButtonPressed:(id)sender
{
    //TODO: cambiar todos los textEng por condicionales una ves se guarde en lenguaje ya sea en BD o en NSUserDefaults (aplica a toda la app)
    self.currentQuestion--;
    self.questionLabel.text = [self.questions[self.currentQuestion] textEng];
    //[self selectAnswerButtonAtIndex:[self answerForQuestionAtIndex:self.currentQuestion]];
    
    if (self.lastAnsweredQuestion > self.currentQuestion)
    {
        [self nextButtonHidden:NO];
    }
    
    if (self.currentQuestion == 0)
    {
        [self previousButtonHidden:YES];
    }
    else if (self.currentQuestion == self.questions.count - 2)
    {
        [self confirmButtonHidden:YES];
        [self nextButtonHidden:NO];
    }

}


- (IBAction)confirmAnswersButtonPressed:(id)sender
{
    //TODO: enviar las preguntas al sever, obtener los resultados y actualizar modelo y vistas y db
    
    
}


- (void)nextButtonHidden:(BOOL) hidden
{
    self.nextQuestionButton.hidden = hidden;
    self.nextQuestionButton.enabled = !hidden;
}


- (void)previousButtonHidden:(BOOL) hidden
{
    self.previousQuestionButton.hidden = hidden;
    self.previousQuestionButton.enabled = !hidden;
}


- (void)confirmButtonHidden:(BOOL) hidden
{
    self.confirmAnswersButton.hidden = hidden;
    self.confirmAnswersButton.enabled = !hidden;
    self.confirmAnswersLabel.hidden = hidden;
}


- (void)deselectAnswerButtons
{
    self.yesButton.selected = NO;
    self.maybeButton.selected = NO;
    self.noButton.selected = NO;
}


- (void)selectAnswerButtonAtIndex:(NSInteger)answerIndex
{
    //TODO
    
}


- (NSInteger)answerForQuestionAtIndex:(NSInteger)answerIndex
{
    //TODO
    return 0;
}


- (void)resetQuestions
{
    NSLog(@"resetQuestions");

}

#pragma mark - BLControllerDelegate

- (void)questionsResponseReceived:(NSArray *)questions
{
    self.questions = questions;
    self.questionLabel.text = [[questions objectAtIndex:0] textEng];
}




#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1)
    {
        [self resetQuestions];
    }
}

#pragma mark - Other

- (void) setupButtons
{
    [self.navBarMenuOptionButton setup];
    [self.noButton setup];
    [self.yesButton setup];
    [self.maybeButton setup];
}

- (NSMutableArray*) getQuestions
{
    //TODO: Crear clase de acceso a datos que retorne las preguntas
    return [NSMutableArray arrayWithObjects:
            @"¿Cree que el sector tecnologico se encuentra en una posicion vulnerable?",
            @"¿Crees que la industria automotriz está en su auge?",
            @"¿Cree que la agroindustria va en declive?", nil];
}

- (NSInteger) getLastAnsweredQuestion
{
    //TODO: obtener la ultima pregunta contestada de bd local, server o ns_user_defaults
    return 0;
}


@end
	