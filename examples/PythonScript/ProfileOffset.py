# Example code for single profile input and single profile output python script

def pt_single_plot_to_single_plot(srcXs, srcYs, offset : float = 0.0):
    """ Add offset to ImageJ Profile
    Parameters:
        srcXs: [1D numpy array] input profile X-axis data
        srcYs: [1D numpy array] input profile Y-axis data
        offset: [float] offset to be added to the input profile
    Return:
        (dstXs, dstYs)
        dstXs: [1D numpy array] output profile X-axis data
        dstYs: [1D numpy array] output profile Y-axis data
    """
    dstXs = srcXs
    dstYs = srcYs + offset
    return (dstXs, dstYs)