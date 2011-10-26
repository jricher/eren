/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

/**
 * The numbers returned from the functions are based on some charts from the
 * Rand report "Recommended Infrastructure Standards for Mass Antibiotic Dispensing".
 * Figure 5.2 shows staff levels versus throughput for each of 6 standards of care
 * Figure 5.1 shows the type of staff needed for each option at a throughput of 500 people per hour.
 *
 * In Figure 5.1, support staff levels remain constant for all care options
 * with rising numbers of nonmedical staff replacing nurses, allied health, and
 * pharmacists. Here, we assume that support staff and nonmedical staff are the same,
 * so we need more support staff and less medical staff as the care option number increases.

 * Figure 5.1 shows 8 support staff required for 500/hour throughput for all options.
 * The required support staff at other throughputs is proportional, but the same
 * at all care options (3.2:200, 4:250, 4.8:300, 6.4:400, 8:500, 9.6:600, 11.2:700, 12.8:800, 14.4:900, 16:1000 , rounded up or down)
 * @author jwinston
 */
public class ThroughputHelper {

    public static int getThroughput(int careLevel, int medStaffLevel, int supportStaffLevel,
            int staffPerformancePenalty) {
        int throughput = 0;
        switch(careLevel) {
            case 1: 
                throughput =  getThrougputCL1(medStaffLevel, supportStaffLevel);
                break;
            case 2:
            case 3:
                throughput = getThrougputCL23(medStaffLevel, supportStaffLevel);
                break;
            case 4:
            case 5:
                throughput = getThroughputCL45(medStaffLevel, supportStaffLevel);
                break;
            case 6:
                throughput = getThroughputCL6(medStaffLevel, supportStaffLevel);
                break;
            default:
                return 0;
        }
        throughput = throughput - (int)((throughput * staffPerformancePenalty)/100.0);
        // don't let it go negative
        if (throughput < 10)
            throughput = 10;
        return throughput;
    }


    private static int getThrougputCL1(int medStaffLevel, int supportStaffLevel) {
        // Medical staff can fill in for support staff, but not vice versa

        if (medStaffLevel < 20)
            return 0;
        if (medStaffLevel < 31) {
            if (supportStaffLevel < 3)
                return 0;
            return 200;
        }
        if (medStaffLevel < 37) {
            if (supportStaffLevel < 4)
                return 200;
            return 250;
        }
        if (medStaffLevel < 38) {
            if (supportStaffLevel < 5)
                return 250;
            return 300;
        }
        if (medStaffLevel < 44) {
            if (supportStaffLevel < 6)
                return 300;
            return 400;
        }
        if (medStaffLevel < 54) {
            if (supportStaffLevel < 8)
                return 400;
            return 500;
        }
        if (medStaffLevel < 60) {
            if (supportStaffLevel < 10)
                return 500;
            return 600;
        }
        if (medStaffLevel < 64) {
            if (supportStaffLevel < 11)
                return 600;
            return 700;
        }
        if (medStaffLevel < 77) {
            if (supportStaffLevel < 13)
                return 700;
            return 800;
        }
        if (medStaffLevel < 86) {
            if (supportStaffLevel < 14)
                return 800;
            return 900;
        }
        if (supportStaffLevel < 16)
            return 900;
        return 1000;
    }

    private static int getThrougputCL23(int medStaffLevel, int supportStaffLevel) {
        if (medStaffLevel < 18)
            return 0;
        if (medStaffLevel < 27) {
            if (supportStaffLevel < 3)
                return 0;
            return 200;
        }
        if (medStaffLevel < 32) {
            if (supportStaffLevel < 4)
                return 200;
            return 250;
        }
        if (medStaffLevel < 33) {
            if (supportStaffLevel < 5)
                return 250;
            return 300;
        }
        if (medStaffLevel < 39) {
            if (supportStaffLevel < 6)
                return 300;
            return 400;
        }
        if (medStaffLevel < 48) {
            if (supportStaffLevel < 8)
                return 400;
            return 500;
        }
        if (medStaffLevel < 50) {
            if (supportStaffLevel < 10)
                return 500;
            return 600;
        }
        if (medStaffLevel < 57) {
            if (supportStaffLevel < 11)
                return 600;
            return 700;
        }
        if (medStaffLevel < 66) {
            if (supportStaffLevel < 13)
                return 700;
            return 800;
        }
        if (medStaffLevel < 74) {
            if (supportStaffLevel < 14)
                return 800;
            return 900;
        }
        if (supportStaffLevel < 16)
	    return 900;
        return 1000;

    }

    private static int getThroughputCL45(int medStaffLevel, int supportStaffLevel) {
        if (medStaffLevel < 17)
            return 0;
        if (medStaffLevel < 21){
            if (supportStaffLevel < 3)
                return 0;
            return 200;
        }
        if (medStaffLevel < 25) {
            if (supportStaffLevel < 4)
                return 200;
        }
        if (medStaffLevel < 28) {
            if (supportStaffLevel < 5)
                return 250;
            return 300;
        }
        if (medStaffLevel < 29) {
            if (supportStaffLevel < 6)
                return 300;
            return 400;
        }
        if (medStaffLevel < 35) {
            if (supportStaffLevel < 8)
                return 400;
            return 500;
        }
        if (medStaffLevel < 39){
            if (supportStaffLevel < 10)
                return 500;
            return 600;
        }
        if (medStaffLevel < 42) {
            if (supportStaffLevel < 11)
                return 600;
            return 700;
        }
        if (medStaffLevel < 46) {
            if (supportStaffLevel < 13)
                return 700;
            return 800;
        }
        if (medStaffLevel < 49) {
            if (supportStaffLevel < 14)
                return 800;
            return 900;
        }
        if (supportStaffLevel < 16)
	    return 900;
        return 1000;
    }

    private static int getThroughputCL6(int medStaffLevel, int supportStaffLevel) {
        if (medStaffLevel < 14)
            return 0;
        if (medStaffLevel < 16){
            if (supportStaffLevel < 3)
                return 0;
            return 200;
        }
        if (medStaffLevel < 18) {
            if (supportStaffLevel < 4)
                return 200;
            return 250;
        }
        if (medStaffLevel < 19) {
            if (supportStaffLevel < 5)
                return 250;
            return 300;
        }
        if (medStaffLevel < 22) {
            if (supportStaffLevel < 6)
                return 300;
            return 400;
        }
        if (medStaffLevel < 22) {
            if (supportStaffLevel < 8)
                return 400;
            return 500;
        }
        if (medStaffLevel < 29) {
            if (supportStaffLevel < 10)
                return 500;
            return 600;
        }
        if (medStaffLevel < 32) {
            if (supportStaffLevel < 11)
                return 600;
            return 700;
        }
        if (medStaffLevel < 36) {
            if (supportStaffLevel < 13)
                return 700;
            return 800;
        }
        if (medStaffLevel < 39) {
            if (supportStaffLevel < 14)
                return 800;
            return 900;
        }
        if (supportStaffLevel < 16)
	    return 900;
        return 1000;
    }

    /**
     *
     * @param standardOfCare
     * @return Made up penalty for the given standard of care
     */
    public static int getCarePenalty(int standardOfCare) {
        switch (standardOfCare) {
            case 1: return 0;
            case 2: return 2;
            case 3: return 3;
            case 4: return 3;
            case 5: return 4;
            case 6: return 6;
        }
        return -1;
    }
}
