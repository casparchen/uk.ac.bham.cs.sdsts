package uk.ac.bham.cs.sdsts.core.sitra_rules;


import java.util.ArrayList;

import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.InteractionOperatorKind;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Attr;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.PrimSig;

import uk.ac.bham.cs.sdsts.core.synthesis.AlloyModel;
import uk.ac.bham.sitra.Rule;
import uk.ac.bham.sitra.RuleNotFoundException;
import uk.ac.bham.sitra.Transformer;

@SuppressWarnings({ "rawtypes", "restriction" })
public class CombinedFragment2Alloy implements Rule {

	@Override
	public boolean check(Object source) {
		if (source instanceof CombinedFragmentImpl) {
			return true;
		} else
			return false;
	}

	@Override
	public Object build(Object source, Transformer t) {
		try {
			CombinedFragment combinedFragment = (CombinedFragment) source;
			// add abstract for combinedFragment
			// abstract sig COMBINEDFRAGMENT {}
			PrimSig combinedFragment_abstract = new PrimSig("COMBINEDFRAGMENT", Attr.ABSTRACT);
			combinedFragment_abstract = (PrimSig) AlloyModel.getInstance().addAbstract(combinedFragment_abstract);
			
			// add sig for combinedFragment type
			PrimSig cfType = null;
			if(combinedFragment.getInteractionOperator() == InteractionOperatorKind.PAR_LITERAL){
				cfType = new PrimSig("CombiedFragmentType_Par", Attr.ONE) ;
			}
			if(combinedFragment.getInteractionOperator() == InteractionOperatorKind.ALT_LITERAL){
				cfType = new PrimSig("CombinedFragmentType_Alt", Attr.ONE);
			}
			if(combinedFragment.getInteractionOperator() == InteractionOperatorKind.LOOP_LITERAL){
				cfType = new PrimSig("CombinedFragmentType_Loop", Attr.ONE);
			}
			cfType = (PrimSig) AlloyModel.getInstance().addSig(cfType);
			
			// add sig for combinedFragment
			// one sig SDid_CFname {op1:operand1, op2:operand2 ...}
			PrimSig combinedFragmentSig = new PrimSig(AlloyModel.getInstance().getSD_prefix() + combinedFragment.getName(), combinedFragment_abstract, Attr.ONE);
			combinedFragmentSig.addField("type", cfType);
			int i = 1;
			for (InteractionOperand interactionOperand : combinedFragment.getOperands()) {
				PrimSig interactionOperandSig = (PrimSig) t.transform(interactionOperand);
				combinedFragmentSig.addField("op" + i++, interactionOperandSig);
			}
			AlloyModel.getInstance().addSig(combinedFragmentSig);
			
			
			// add fact inside the combined fragment for Par
			if(combinedFragment.getInteractionOperator() == InteractionOperatorKind.PAR_LITERAL){
				for (i = 0; i < combinedFragment.getOperands().size(); i++) {
					for (int j = i + 1; j < combinedFragment.getOperands().size(); j++) {
						InteractionOperand op1 = combinedFragment.getOperands().get(i);
						InteractionOperand op2 = combinedFragment.getOperands().get(j);
						for (InteractionFragment fragment : op1.getFragments()) {
							if(fragment instanceof MessageOccurrenceSpecification){
								
							}
						}
						MessageOccurrenceSpecification mo1 = (MessageOccurrenceSpecification) op1.getFragments().get(0);
						MessageOccurrenceSpecification mo2 = (MessageOccurrenceSpecification) op2.getFragments().get(0);
						
					}
				}
			}
			
			
			// add fact inside the combined fragment for Alt
			String fact_remove_relation = "// remove the relations among evens in different operands of CF_Alt: " + combinedFragmentSig.toString();
			if(combinedFragment.getInteractionOperator() == InteractionOperatorKind.ALT_LITERAL){
				for (i = 0; i < combinedFragment.getOperands().size(); i++) {
					for (int j = i+1; j < combinedFragment.getOperands().size(); j++) {
						InteractionOperand op1 = combinedFragment.getOperands().get(i);
						InteractionOperand op2 = combinedFragment.getOperands().get(j);
						PrimSig op1Sig = (PrimSig) AlloyModel.getInstance().getSigByName(AlloyModel.getInstance().getSD_prefix() + op1.getName());
						PrimSig op2Sig = (PrimSig) AlloyModel.getInstance().getSigByName(AlloyModel.getInstance().getSD_prefix() + op2.getName());
//						fact {all _E:sd1_InteractionOperand.cov | no _E1: sd1_InteractionOperand0.cov | _E in _E1.ISBEFORE}
//						fact {all _E:sd1_InteractionOperand0.cov | no _E1: sd1_InteractionOperand.cov | _E in _E1.ISBEFORE}
						String fact = String.format("fact {all _E:%s.cov | no _E1: %s.cov | _E in _E1.ISBEFORE}", op1Sig, op2Sig);
						String fact1 = String.format("fact {all _E:%s.cov | no _E1: %s.cov | _E in _E1.ISBEFORE}", op2Sig, op1Sig);
						fact_remove_relation += "\n" + fact;
						fact_remove_relation += "\n" + fact1;
					}
				}
			}		
			AlloyModel.getInstance().addFact(fact_remove_relation);

			
			
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void setProperties(Object target, Object source, Transformer t) {
		
	}

}
