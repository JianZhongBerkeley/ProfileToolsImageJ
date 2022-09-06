# Example code of profile processing with two profiles input and single profile output 

def  pt_double_plot_to_single_plot(srcXs1, srcYs1, srcXs2, srcYs2, weight1 : float = 1.0, weight2: float = 1.0):
    """ Calculate weighted sume of two profiles
    Parameters:
        srcXs1: [1D numpy array] 1st source profile X-axis data
        srcYs1: [1D numpy array] 1st source profile Y-axis data
        srcXs2: [1D numpy array] 2nd source profile X-axis data
        srcYs2: [1D numpy array] 2nd source profile Y-axis data
        weight1: [float] weight for 1st source profile
        weight2: [float] weight for 2nd source profile
    Return
        (dstXs, dstYs)
        dstXs: [1D numpy array] result profile X-axis data
        dstYs: [1D numpy array] result profile Y-axis data
    """
    import numpy 
    N = min(srcYs1.size, srcYs2.size)
    dstXs = srcXs1[0:N]
    dstYs = weight1 * srcYs1[0:N] + weight2 * srcYs2[0:N]
    return (dstXs, dstYs)     
    